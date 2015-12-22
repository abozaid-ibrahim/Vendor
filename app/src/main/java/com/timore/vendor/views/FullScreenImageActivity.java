package com.timore.vendor.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.timore.vendor.R;
import com.timore.vendor.control.Image;
import com.timore.vendor.control.SuperActivity;
import com.timore.vendor.control.VAR;

public class FullScreenImageActivity extends SuperActivity implements View.OnClickListener {

    private ProgressBar progress;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        imageView = (ImageView) findViewById(R.id.fullscreen_imageview);
        progress = (ProgressBar) findViewById(R.id.fallscreen_progressBar);
        findViewById(R.id.fallscreen_cancel).setOnClickListener(this);
        if (getIntent().getExtras() != null) {
            String url = getIntent().getExtras().getString(VAR.KEY_URL);
            System.err.println("URL "+url);

//        url = "http://cdn.cavemancircus.com//wp-content/uploads/images/2014/november/pretty_girls_4/pretty_girls_1.jpg";
            Image.obj(this).setImage(imageView, url, progress);
//            Image.obj(this).setImage(imageView,url, R.drawable.logo_2);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fallscreen_cancel:
                finish();
                break;

        }
    }
}
