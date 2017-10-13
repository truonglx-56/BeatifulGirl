package com.tspro.android.beatifulgirl;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tspro.android.beatifulgirl.adapter.DrawerAdapter;
import com.tspro.android.beatifulgirl.fragment.HomePageFragment;
import com.tspro.android.beatifulgirl.model.DrawerItem;
import com.tspro.android.beatifulgirl.util.DialogGetTokenUtils;

public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ListView mDrawerListCustom;
    private View mViewListView;
    private List<DrawerItem> mDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Handler mHandler;
    private static final int SUBSCRIBE_VIEW = 3;


    private boolean mShouldFinish = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DialogGetTokenUtils dialogGetTokenUtils = new DialogGetTokenUtils(this);
        dialogGetTokenUtils.showDialog();
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                if (key.equals("link")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value.toString()));
                    startActivity(browserIntent);
                }
            }
        }


        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundResource(R.color.material_grey_700);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mTitle = mDrawerTitle = getTitle();
        mDrawerList = (ListView) findViewById(R.id.list_view);
        mDrawerListCustom = (ListView) findViewById(R.id.list_view2);
        mViewListView = (View) findViewById(R.id.parent);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        prepareNavigationDrawerItems();

        mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerItems, true));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerListCustom.setAdapter(new DrawerAdapter(this, mDrawerItems, true));
        mDrawerListCustom.setOnItemClickListener(new DrawerItemClickListener());

        mViewListView.setBackgroundResource(R.drawable.ic_left_menu_girl);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mHandler = new Handler();

        if (savedInstanceState == null) {
            int position = 0;
            selectItem(position, mDrawerItems.get(position).getTag());
            mDrawerLayout.openDrawer(mViewListView);
        }

    }

    @Override
    public void onBackPressed() {
        if (!mShouldFinish && !mDrawerLayout.isDrawerOpen(mViewListView)) {
            Toast.makeText(getApplicationContext(), R.string.confirm_exit,
                    Toast.LENGTH_SHORT).show();
            mShouldFinish = true;
            mDrawerLayout.openDrawer(mViewListView);
        } else if (!mShouldFinish && mDrawerLayout.isDrawerOpen(mViewListView)) {
            mDrawerLayout.closeDrawer(mViewListView);
        } else {
            super.onBackPressed();
        }
    }

    private void prepareNavigationDrawerItems() {
        mDrawerItems = new ArrayList<>();
        mDrawerItems.add(new DrawerItem(R.string.material_icon_home,
                R.string.drawer_title_homepage,
                DrawerItem.DRAWER_ITEM_TAG_HOMEPAGE));
        mDrawerItems.add(new DrawerItem(R.string.material_icon_like,
                R.string.drawer_title_like,
                DrawerItem.DRAWER_ITEM_TAG_LIKE));
        mDrawerItems.add(new DrawerItem(R.string.material_icon_bookmark,
                R.string.drawer_title_downloaded,
                DrawerItem.DRAWER_ITEM_TAG_DOWNLOAD));
        mDrawerItems.add(new DrawerItem(R.string.material_icon_info,
                R.string.drawer_title_about,
                DrawerItem.DRAWER_ITEM_TAG_ABOUT));

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        SharedPreferences subscribe = getApplicationContext().getSharedPreferences("SUBSCRIBE", MODE_PRIVATE);
        int numOfViews = subscribe.getInt("numOfViews", 1);
        savedInstanceState.putInt("numOfViews", numOfViews);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int numOfViews = savedInstanceState.getInt("numOfViews");
        SharedPreferences subscribe = getApplicationContext().getSharedPreferences("SUBSCRIBE", MODE_PRIVATE);
        SharedPreferences.Editor editor = subscribe.edit();
        editor.putInt("numOfViews", numOfViews);
        editor.apply();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position, mDrawerItems.get(position).getTag());
        }
    }

    private void selectItem(int position, int drawerTag) {
        Fragment fragment = getFragmentByDrawerTag(drawerTag);
        commitFragment(fragment);

        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerItems.get(position).getTitle());
        mDrawerLayout.closeDrawer(mViewListView);


    }

    private Fragment getFragmentByDrawerTag(int drawerTag) {
        Fragment fragment;
        if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_HOMEPAGE) {
            fragment = HomePageFragment.newInstance();
        } else if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_LIKE) {
            fragment = HomePageFragment.newInstance();
        } else if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_DOWNLOAD) {
            fragment = HomePageFragment.newInstance();
        } else if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_ABOUT) {
            fragment = HomePageFragment.newInstance();
        } else {
            fragment = new Fragment();
        }
        mShouldFinish = false;
        return fragment;
    }

    private class CommitFragmentRunnable implements Runnable {

        private Fragment fragment;

        public CommitFragmentRunnable(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void run() {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        }
    }

    public void commitFragment(Fragment fragment) {
        // Using Handler class to avoid lagging while
        // committing fragment in same time as closing
        // navigation drawer
        mHandler.post(new CommitFragmentRunnable(fragment));
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


}