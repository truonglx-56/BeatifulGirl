package com.tspro.project.girl;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tspro.project.girl.fragment.WizardTokenFragment;

import java.util.Arrays;
import java.util.List;


public class WizardTokenActivity extends AppCompatActivity {

    private MyPagerAdapter adapter;
    private ViewPager pager;
    private TextView title;
    private TextView text;
    private TextView navigator;
    private TextView button;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_travel);

        currentItem = 0;

        pager = (ViewPager) findViewById(R.id.activity_wizard_travel_pager);
        title = (TextView) findViewById(R.id.activity_wizard_travel_title);
        text = (TextView) findViewById(R.id.activity_wizard_travel_text);
        navigator = (TextView) findViewById(R.id.activity_wizard_travel_possition);
        button = (TextView) findViewById(R.id.activity_wizard_travel_button);

        String[] wizard = new String[]{
                getString(R.string.token_wizard_0),
                getString(R.string.token_wizard_1),
                getString(R.string.token_wizard_2),
                getString(R.string.token_wizard_3),
                getString(R.string.token_wizard_4),
                getString(R.string.token_wizard_5),
                getString(R.string.token_wizard_6),
                getString(R.string.token_wizard_7),
                getString(R.string.token_wizard_8),
                getString(R.string.token_wizard_9),
                getString(R.string.token_wizard_10),

        };

        adapter = new MyPagerAdapter(getSupportFragmentManager(), Arrays.asList(wizard));
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentItem);

        setNavigator();
        //	title.setText("Fragment Example " + (currentItem + 1));
        //	text.setText("Text for Fragment Example " + (currentItem + 1) + " " + getString(R.string.lorem_ipsum_short));

        pager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {
                // TODO Auto-generated method stub
                setNavigator();
            }
        });

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url = "https://facebook.com";
                Uri uri = Uri.parse("googlechrome://navigate?url=" + url);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                if (i.resolveActivity(getPackageManager()) == null) {
                    i.setData(Uri.parse(url));
                }
                startActivity(i);
                finish();
//                Toast.makeText(WizardTokenActivity.this, "SIGN IN",
//                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setNavigator() {
        String navigation = "";
        for (int i = 0; i < adapter.getCount(); i++) {
            if (i == pager.getCurrentItem()) {
                navigation += getString(R.string.material_icon_point_full)
                        + "  ";
            } else {
                navigation += getString(R.string.material_icon_point_empty)
                        + "  ";
            }
        }
        navigator.setText(navigation);
        title.setText((pager.getCurrentItem() + 1) + "");
        text.setText(adapter.getMyStringToken().get(pager.getCurrentItem()));
    }

    public void setCurrentSlidePosition(int position) {
        this.currentItem = position;
    }

    public int getCurrentSlidePosition() {
        return this.currentItem;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        public List<String> getMyStringToken() {
            return myStringToken;
        }

        private List<String> myStringToken;

        public MyPagerAdapter(FragmentManager fm, List<String> tokens) {
            super(fm);
            this.myStringToken = tokens;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 11;
        }

        @Override
        public Fragment getItem(int position) {
            return WizardTokenFragment.newInstance(position);
            /*if (position == 0) {
                return WizardTokenFragment.newInstance(position);
			} else if (position == 1) {
				return WizardTokenFragment.newInstance(position);
			} else {
				return WizardTokenFragment.newInstance(position);
			}*/
        }
    }
}