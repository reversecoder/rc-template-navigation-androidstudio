package com.reversecoder.rcprojecttemplate.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.reversecoder.library.customview.application.BaseActivity;
import com.reversecoder.rcprojecttemplate.R;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_splash);
        setActivityTitle("Splash Screen");
        setToolbar(false);
        startSplashScreenTimer();
    }

    private void startSplashScreenTimer() {
        CountDownTimer mSplashTimer = new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                goHomeScreen();

            }
        };
        mSplashTimer.start();
    }

    private void goHomeScreen() {
        Intent intent = new Intent(getParentContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
