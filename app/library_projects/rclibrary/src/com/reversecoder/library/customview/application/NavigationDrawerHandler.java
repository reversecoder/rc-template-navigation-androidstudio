package com.reversecoder.library.customview.application;

import android.support.design.widget.NavigationView;

import com.reversecoder.library.customview.memoryleakhandler.ParentActivity;

public abstract class NavigationDrawerHandler extends ParentActivity {

    public abstract void setNavigationDrawerHeaderView(int navigationDrawerHeaderView);

    public abstract void setNavigationDrawerMenuView(int navigationDrawerMenuView);

    public abstract void setNavigationDrawerMenuListener(NavigationView.OnNavigationItemSelectedListener navigationDrawerMenuListener);

}
