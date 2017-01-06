package com.reversecoder.library.customview.application;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reversecoder.library.R;
import com.reversecoder.library.customview.customfonttextview.CustomFontTextView;
import com.reversecoder.library.customview.memoryleakhandler.ParentActivity;
import com.reversecoder.library.customview.utils.ToolbarColorizeManager;

/**
 * Created by rashed on 3/7/16.
 */
public class BaseActivity extends ParentActivity{

    private LayoutInflater inflater;
    public FrameLayout baseLayout;
    private FrameLayout inflateLayout;
    LinearLayout addViewIntoAppbarLayout;
    private CustomFontTextView txtActivityTitle;
    private Toolbar toolbar;
    private ImageView burgerMenu,btnHelp;
    private ProgressDialog loadingDialog;
    boolean isAppBarEnabled=false;
    boolean isToolBarScrollingEnabled=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        inflater = LayoutInflater.from(getParentContext());
        baseLayout = (FrameLayout) findViewById(R.id.root);
        inflateLayout = (FrameLayout) findViewById(R.id.activity_content);
        txtActivityTitle = (CustomFontTextView) findViewById(R.id.txt_title);
        burgerMenu= (ImageView) findViewById(R.id.burger_menu);
        btnHelp= (ImageView) findViewById(R.id.btn_help);

        // set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setToolbar(true);
    }

     public void setActivityTitle(String title) {
         txtActivityTitle.setText(title);
     }

    public View setActivityLayout(int layout){
        View layoutView = (View) inflater.inflate(layout, inflateLayout, true);
        return layoutView;
    }

    public void setBurgerMenu(boolean isMenu){
        if(isMenu){
            burgerMenu.setVisibility(View.VISIBLE);
        }else{
            burgerMenu.setVisibility(View.GONE);
        }
    }

    public void setHelpButton(){
        btnHelp.setVisibility(View.VISIBLE);
    }

    public void removeHelpButton(){
        btnHelp.setVisibility(View.GONE);
    }

    public View getHelpButton(){
        return btnHelp;
    }

    //remove hamburger menu
    public void removeHamburgerMenu(ActionBarDrawerToggle toggle) {
        toggle.setDrawerIndicatorEnabled(false);
    }

    //lock drawer
    public void lockDrawer(DrawerLayout drawer) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    //remove title
    public void removeTitle(){
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
    }

    public void setToolbar(boolean isToolbar){
        if(isToolbar){
            baseLayout.setFitsSystemWindows(true);
            setStatusBarColor(R.color.colorPrimaryDark);
            ToolbarColorizeManager.colorizeToolbar(BaseActivity.this,toolbar, R.color.white);
            setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            toolbar.setVisibility(View.VISIBLE);
        }else{
            setTranslucentStatusBar();
            baseLayout.setFitsSystemWindows(false);
            toolbar.setVisibility(View.GONE);
        }
    }

    public void setToolbarScrollingEnabled(boolean isEnabled) {
        isToolBarScrollingEnabled=isEnabled;
        if(toolbar==null){
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(isToolBarScrollingEnabled ? (AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS) : 0);
    }

    public boolean isToolBarScrollingEnabled() {
        return isToolBarScrollingEnabled;
    }


    public View setAppBarLayoutView(int layoutResID){
        isAppBarEnabled=true;
        if(addViewIntoAppbarLayout==null){
            addViewIntoAppbarLayout=(LinearLayout) baseLayout.findViewById(R.id.add_view_into_appbarlayout);
        }
        addViewIntoAppbarLayout.removeAllViews();
        return getLayoutInflater().inflate(layoutResID, addViewIntoAppbarLayout,true);
    }

    public boolean isAppBarEnabled() {
        return isAppBarEnabled;
    }

    public void removeAppBarLayout(){
        if(isAppBarEnabled()){
            isAppBarEnabled=false;
            if(addViewIntoAppbarLayout==null){
                addViewIntoAppbarLayout=(LinearLayout) baseLayout.findViewById(R.id.add_view_into_appbarlayout);
            }
            addViewIntoAppbarLayout.removeAllViews();
            setToolbarScrollingEnabled(false);
        }
    }

    public void setNavigationOnClickListener(View.OnClickListener navigationOnClickListener){
        toolbar.setNavigationOnClickListener(navigationOnClickListener);
    }

    public void setTranslucentStatusBar(){
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

    public ProgressDialog getLoadingDialog(){
        if(loadingDialog==null) {
            loadingDialog = new ProgressDialog(getParentContext());
        }
        return loadingDialog;
    }
}
