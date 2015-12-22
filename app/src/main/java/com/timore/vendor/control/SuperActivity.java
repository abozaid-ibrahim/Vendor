package com.timore.vendor.control;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Abuzeid on 11/27/2015.
 */
public class SuperActivity extends AppCompatActivity {
    public static final String POST = "post";
    public static final String USER = "user";
    public static final String PROFILE = "prof";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void setToolBar(View view, boolean canBack) {
        if (view == null)
            return;
        Toolbar toolbar = (Toolbar) view;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(canBack);


    }

}
