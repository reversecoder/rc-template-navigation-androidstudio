package com.reversecoder.rcprojecttemplate.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reversecoder.rcprojecttemplate.R;

public class ReportFormatFragment extends Fragment {

    private View parentView;

    public static ReportFormatFragment newInstance() {
        return new ReportFormatFragment();
    }

    public ReportFormatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_report_format, container, false);
        return parentView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
