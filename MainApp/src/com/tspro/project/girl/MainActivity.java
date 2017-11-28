package com.tspro.project.girl;


import java.util.ArrayList;
import java.util.List;


import android.Manifest;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tspro.project.girl.adapter.DrawerAdapter;
import com.tspro.project.girl.adapter.DrawerAdapterMore;
import com.tspro.project.girl.fragment.AboutFragment;
import com.tspro.project.girl.fragment.ExtensionFragment;
import com.tspro.project.girl.fragment.FavoriteFragment;
import com.tspro.project.girl.fragment.HomePageFragment;
import com.tspro.project.girl.model.DrawerItem;
import com.tspro.project.girl.model.Group;
import com.tspro.project.girl.util.DialogGetTokenUtils;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class MainActivity extends AppCompatActivity implements DialogGetTokenUtils.Callback, DrawerAdapterMore.Callback {
    public static int STORAGE_WRITE_PERMISSION_BITMAP_SHARE = 0x1;

    private ListView mDrawerList;
    private ListView mDrawerListCustom;
    private View mViewListView;
    private List<DrawerItem> mDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private Handler mHandler;


    private boolean mShouldFinish = false;
    private AlertDialog mAlertDialog;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundResource(R.color.material_grey_900);
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

        mViewListView = findViewById(R.id.parent);
        mViewListView.setBackgroundResource(R.drawable.ic_left_menu_girl);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        prepareNavigationDrawerItems();

        mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerItems, true));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mHandler = new Handler();

        if (savedInstanceState == null) {
            int position = 0;
            selectItem(position, mDrawerItems.get(position).getTag());
            mDrawerLayout.openDrawer(mViewListView);
        }

        permission();

        BeatifulApplication.getInstance().initDownload();
        registerReceiver(Downloader.getInstance().onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        setAdapterMore();

    }

    private DrawerAdapterMore drawerAdapterMore;

    private void setAdapterMore() {
        ArrayList<DrawerItem> drawerItems = new ArrayList<>();

        drawerItems.add(new DrawerItem(R.string.material_icon_add,
                getApplicationContext().getString(R.string.drawer_title_add), DrawerItem.DRAWER_ITEM_DEFAULT));

        List<Group> l = BeatifulApplication.getInstance().getDataFromSharedPreferences();
        DrawerItem drawerItem;

        for (int i = 0; i < l.size(); i++) {
            drawerItem = new DrawerItem(R.string.material_icon_info, l.get(i).getMask(), l.get(i).hashCode());
            drawerItem.setGroup(l.get(i));
            drawerItems.add(drawerItem);
        }

        drawerAdapterMore = new DrawerAdapterMore(this, drawerItems);
        mDrawerListCustom.setAdapter(drawerAdapterMore);
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
                getApplicationContext().getString(R.string.drawer_title_homepage),
                DrawerItem.DRAWER_ITEM_TAG_HOMEPAGE));
        mDrawerItems.add(new DrawerItem(R.string.material_icon_like,
                getApplicationContext().getString(R.string.drawer_title_like),
                DrawerItem.DRAWER_ITEM_TAG_LIKE));
       /* mDrawerItems.add(new DrawerItem(R.string.material_icon_bookmark,
                R.string.drawer_title_downloaded,
                DrawerItem.DRAWER_ITEM_TAG_DOWNLOAD));*/
        mDrawerItems.add(new DrawerItem(R.string.material_icon_info,
                getApplicationContext().getString(R.string.drawer_title_about),
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

    @Override
    public synchronized void callback(Group group) {
        if (drawerAdapterMore != null) {
            DrawerItem drawerItem = new DrawerItem(R.string.material_icon_info, group.getMask(), group.hashCode());
            drawerItem.setGroup(group);
            drawerAdapterMore.addDrawerItems(drawerItem);
            drawerAdapterMore.notifyDataSetChanged();
        }
       /* this.mCurrentToken = group.getToken();
        this.mCurrentGroupId = String.valueOf(group.getId());*/
    }


    @Override
    public void excute(final int pos, final DrawerItem drawerItem) {
        //todo
        selectItemMore(pos, drawerItem);
    }

    @Override
    public void delete() {
        if (drawerAdapterMore != null) {
            ArrayList<DrawerItem> drawerItems = new ArrayList<>();

            drawerItems.add(new DrawerItem(R.string.material_icon_add,
                    getApplicationContext().getString(R.string.drawer_title_add), DrawerItem.DRAWER_ITEM_DEFAULT));

            List<Group> l = BeatifulApplication.getInstance().getDataFromSharedPreferences();
            DrawerItem drawerItem;

            for (int i = 0; i < l.size(); i++) {
                drawerItem = new DrawerItem(R.string.material_icon_info, l.get(i).getMask(), l.get(i).hashCode());
                drawerItem.setGroup(l.get(i));
                drawerItems.add(drawerItem);
            }
            drawerAdapterMore.setList(drawerItems);
            drawerAdapterMore.notifyDataSetChanged();
        }
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

    private void selectItemMore(int position, DrawerItem drawerItem) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("token_groupID", new String[]{drawerItem.getGroup().getToken(), String.valueOf(drawerItem.getGroup().getId())});

        Fragment fragment = getFragmentByDrawerTag(drawerItem.getTag());
        commitFragment(fragment, bundle);

        mDrawerListCustom.setItemChecked(position, true);
        setTitle(drawerItem.getTitle());
        mDrawerLayout.closeDrawer(mViewListView);
    }

    private Fragment getFragmentByDrawerTag(int drawerTag) {
        Fragment fragment;
        if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_HOMEPAGE) {
            fragment = HomePageFragment.newInstance();
        } else if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_LIKE) {
            fragment = FavoriteFragment.newInstance();
       /* } else if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_DOWNLOAD) {
            fragment = HomePageFragment.newInstance();*/
        } else if (drawerTag == DrawerItem.DRAWER_ITEM_TAG_ABOUT) {
            fragment = AboutFragment.newInstance();
        } else {
            fragment = new ExtensionFragment();
        }
        mShouldFinish = false;
        return fragment;
    }

    private class CommitFragmentRunnable implements Runnable {

        private Fragment fragment;
        private Bundle bundle;

        public CommitFragmentRunnable(Fragment fragment, Bundle bundle) {
            this.fragment = fragment;
            this.bundle = bundle;
        }

        public CommitFragmentRunnable(Fragment fragment) {
            this.fragment = fragment;

        }

        @Override
        public void run() {
            FragmentManager fragmentManager = getSupportFragmentManager();
            // set Fragmentclass Arguments
            fragment.setArguments(bundle);
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

    public void commitFragment(Fragment fragment, Bundle bundle) {
        // Using Handler class to avoid lagging while
        // committing fragment in same time as closing
        // navigation drawer

        mHandler.post(new CommitFragmentRunnable(fragment, bundle));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(Downloader.getInstance().onComplete);
        // accessTokenTracker.stopTracking();
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog("Permission required", rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{permission}, requestCode);
                        }
                    }, getString(android.R.string.ok), null, getString(android.R.string.cancel));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    /**
     * This method shows dialog with given title & content.
     * Also there is an option to pass onClickListener for positive & negative button.
     *
     * @param title                         - dialog title
     * @param message                       - dialog content
     * @param onPositiveButtonClickListener - listener for positive button
     * @param positiveText                  - positive button text
     * @param onNegativeButtonClickListener - listener for negative button
     * @param negativeText                  - negative button text
     */

    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String titles = String.format("<font color='#FFFFFF'>%s</font>", title);
        if (Build.VERSION.SDK_INT >= 24) {
            Html.fromHtml(titles, FROM_HTML_MODE_COMPACT); // for 24 api and more
        } else {
            builder.setTitle(Html.fromHtml(titles));
        }

        String messages = String.format("<font color='#FFFFFF'>%s</font>", message);

        if (Build.VERSION.SDK_INT >= 24) {
            Html.fromHtml(messages, FROM_HTML_MODE_COMPACT); // for 24 api and more
        } else {
            builder.setTitle(Html.fromHtml(messages));
        }
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);

        mAlertDialog = builder.show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getApplicationContext().getColor(R.color.white));
            mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getApplicationContext().getColor(R.color.white));
            mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getColor(R.color.material_grey_800)));

        }
    }

    public void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            /*requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    "permission required for writing external storage!",
                    STORAGE_WRITE_PERMISSION_BITMAP_SHARE);*/
            requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_text),
                    STORAGE_WRITE_PERMISSION_BITMAP_SHARE);
        } else {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}