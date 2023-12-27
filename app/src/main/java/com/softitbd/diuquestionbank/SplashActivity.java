package com.softitbd.diuquestionbank;

import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.softitbd.diuquestionbank.AuthUI.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!PreferencesUtils.getAccessToken(SplashActivity.this).equals("")){
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000); // 2000 milliseconds (2 seconds)
    }
}