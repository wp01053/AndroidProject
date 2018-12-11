package com.study.android.androidproject;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        findViewById(R.id.splash).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, com.study.android.androidproject.KakaoNaviActivity.class));
            finish();
        }, 500);
    }
}
