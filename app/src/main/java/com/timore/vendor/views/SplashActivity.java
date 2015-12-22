package com.timore.vendor.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.timore.vendor.R;
import com.timore.vendor.control.App;

public class SplashActivity extends AppCompatActivity {
    public Intent main;

    private static final int DURATION = 2000;
    private static final int INTERVAL = 20;
    private ProgressBar progressBar;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.splash_progressBar);
        progress();

    }


    void progress() {
        progressBar.setMax(DURATION);

       new CountDownTimer(DURATION, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) (DURATION-millisUntilFinished)+INTERVAL);
            }

            @Override
            public void onFinish() {
                if (App.userId > 0) {
                    main = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    main = new Intent(SplashActivity.this, LoginActivity.class);
                }
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);

            }
        }.start();

    }
}
