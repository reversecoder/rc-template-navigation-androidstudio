package com.reversecoder.library.customview.application;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reversecoder.library.R;
import com.reversecoder.library.customview.customfonttextview.CustomFontTextView;

public class BaseNavigationDrawerActivity extends NavigationDrawerHandler {

    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    Toolbar toolbar;
    LinearLayout addViewIntoAppbarLayout;
    NavigationView navigationView;
    View navigationHeader;
    CustomFontTextView txtActivityTitle;
    DrawerLayout fullLayout;
    FrameLayout activityContainer;
    private int headerView = 0;
    private int menuView = 0;
    private NavigationView.OnNavigationItemSelectedListener menuItemListener = null;
    int mNavItemId = 0;
    MenuItem mNavMenuItem = null;
    public static final String NAV_ITEM_ID = "navItemId";
    ImageView btnHelp;
    boolean isAppBarEnabled = false;
    boolean isToolBarScrollingEnabled = false;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (null == savedInstanceState) {
//            setMenuItemId(0);
////            navigationView.getMenu().getItem(mNavItemId).setChecked(true);
//        } else {
//            setMenuItemId(savedInstanceState.getInt(NAV_ITEM_ID));
//        }
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, getMenuItemId());
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_navigation_drawer, null);
        activityContainer = (FrameLayout) fullLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullLayout);
        setupToolbar();
        setUpNavigationDrawerView();
    }

    @Override
    public void setNavigationDrawerHeaderView(int navigationDrawerHeaderView) {
        headerView = navigationDrawerHeaderView;
    }

    @Override
    public void setNavigationDrawerMenuView(int navigationDrawerMenuView) {
        menuView = navigationDrawerMenuView;
    }

    @Override
    public void setNavigationDrawerMenuListener(NavigationView.OnNavigationItemSelectedListener navigationDrawerMenuListener) {
        menuItemListener = navigationDrawerMenuListener;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        toggle.syncState();
    }

    public void setMenuItem(MenuItem menuItem) {
        mNavMenuItem = menuItem;
    }

    public void setMenuItemId(int menuItemId) {
        mNavItemId = menuItemId;
    }

    public int getMenuItemId() {
        return mNavItemId;
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        removeTitle();
        txtActivityTitle = (CustomFontTextView) findViewById(R.id.txt_title);
        btnHelp = (ImageView) findViewById(R.id.btn_help);
    }

    public void setToolbarScrollingEnabled(boolean isEnabled) {
        isToolBarScrollingEnabled = isEnabled;
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(isToolBarScrollingEnabled ? (AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS) : 0);
    }

    public boolean isToolBarScrollingEnabled() {
        return isToolBarScrollingEnabled;
    }

    public void setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void setStatusBarColor(@ColorRes int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = ContextCompat.getColor(this, statusBarColor);

            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    public View setAppBarLayoutView(int layoutResID) {
        isAppBarEnabled = true;
        if (addViewIntoAppbarLayout == null) {
            addViewIntoAppbarLayout = (LinearLayout) fullLayout.findViewById(R.id.add_view_into_appbarlayout);
        }
        addViewIntoAppbarLayout.removeAllViews();
        return getLayoutInflater().inflate(layoutResID, addViewIntoAppbarLayout, true);
    }

    public boolean isAppBarEnabled() {
        return isAppBarEnabled;
    }

    public void removeAppBarLayout() {
        if (isAppBarEnabled()) {
            isAppBarEnabled = false;
            if (addViewIntoAppbarLayout == null) {
                addViewIntoAppbarLayout = (LinearLayout) fullLayout.findViewById(R.id.add_view_into_appbarlayout);
            }
            addViewIntoAppbarLayout.removeAllViews();
            setToolbarScrollingEnabled(false);
        }
    }

    public void setHelpButton() {
        btnHelp.setVisibility(View.VISIBLE);
    }

    public void removeHelpButton() {
        btnHelp.setVisibility(View.GONE);
    }

    public View getHelpButton() {
        return btnHelp;
    }

    public void setActivityTitle(String title) {
        txtActivityTitle.setText(title);
    }

    private void setUpNavigationDrawerView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setStatusBarColor(R.color.colorPrimaryDark);
                drawer.setFitsSystemWindows(true);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTranslucentStatusBar();
                drawer.setFitsSystemWindows(false);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //set navigation menu
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (headerView > 0) {
            navigationHeader = navigationView.inflateHeaderView(headerView);
        }
        navigationView.inflateMenu(menuView);
        navigationView.setNavigationItemSelectedListener(menuItemListener);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            setStatusBarColor(android.R.color.transparent);
            drawer.setFitsSystemWindows(false);
        } else {
            setStatusBarColor(R.color.colorPrimaryDark);
            drawer.setFitsSystemWindows(true);
        }
    }

    public NavigationView getNavigationView() {
        if (navigationView != null) {
            return navigationView;
        }
        return null;
    }

    public Fragment getCurrentVisibleFragmentObject(String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = (Fragment) fm.findFragmentByTag(fragmentTag);
        return fragment;
    }

    public Fragment getCurrentVisibleFragmentObject(int containerResID) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = (Fragment) fm.findFragmentById(containerResID);
        return fragment;
    }

    public View getNavigationHeaderView() {
        if (navigationHeader != null) {
            return navigationHeader;
        }
        return null;
    }

    public void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    //remove hamburger menu
    public void removeHamburgerMenu() {
        toggle.setDrawerIndicatorEnabled(false);
    }

    //lock drawer
    public void lockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    //remove title
    public void removeTitle() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
