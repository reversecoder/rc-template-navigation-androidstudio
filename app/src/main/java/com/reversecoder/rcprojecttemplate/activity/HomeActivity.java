package com.reversecoder.rcprojecttemplate.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.reversecoder.library.customview.application.BaseNavigationDrawerActivity;
import com.reversecoder.rcprojecttemplate.R;
import com.reversecoder.rcprojecttemplate.fragment.ReportFormatFragment;
import com.reversecoder.rcprojecttemplate.fragment.DailyReportFragment;
import com.reversecoder.rcprojecttemplate.utility.AllConstants;


public class HomeActivity extends BaseNavigationDrawerActivity {
    Handler drawerHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationDrawerHeaderView(R.layout.nav_header_main);
        setNavigationDrawerMenuView(R.menu.home_drawer);
        initNavigationDrawerMenuListener();
        setContentView(R.layout.activity_home);
        setActivityTitle(AllConstants.ACTIVITY_TITLE_DAILY_REPORT);
        initHomeFragment(savedInstanceState);
    }

    private void initHomeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            changeFragment(new DailyReportFragment(), AllConstants.FRAGMENT_TAG_DAILY_REPORT);
            setActivityTitle(AllConstants.ACTIVITY_TITLE_DAILY_REPORT);
            MenuItem menuItem = getNavigationView().getMenu().findItem(R.id.nav_daily_report);
            setMenuItemId(menuItem.getItemId());
            setMenuItem(menuItem);
            menuItem.setChecked(true);
        }
    }

    public void initNavigationDrawerMenuListener() {
        setNavigationDrawerMenuListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                navigate(item.getItemId());
                closeDrawer();
                return true;
            }
        });
    }

    public void changeFragment(Fragment targetFragment, String fragmentName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, fragmentName)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void navigate(final int itemId) {
        drawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setMenuItemId(itemId);
                Fragment fragment;
                if (itemId == R.id.nav_daily_report) {
                    fragment = (DailyReportFragment) getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_TAG_DAILY_REPORT);
                    if (fragment instanceof DailyReportFragment) {
                    } else {
                        changeFragment(new DailyReportFragment(), AllConstants.FRAGMENT_TAG_DAILY_REPORT);
                        setActivityTitle(AllConstants.ACTIVITY_TITLE_DAILY_REPORT);
                    }
                } else if (itemId == R.id.nav_report_format) {
                    fragment = (ReportFormatFragment) getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_TAG_REPORT_FORMAT);
                    if (fragment instanceof ReportFormatFragment) {
                    } else {
                        changeFragment(new ReportFormatFragment(), AllConstants.FRAGMENT_TAG_REPORT_FORMAT);
                        setActivityTitle(AllConstants.ACTIVITY_TITLE_REPORT_FORMAT);
                    }
                }
            }
        }, 250);
    }
}
