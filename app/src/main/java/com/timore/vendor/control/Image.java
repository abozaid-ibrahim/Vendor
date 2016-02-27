package com.timore.vendor.control;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.timore.vendor.R;

/**
 * Created by usear on 11/30/2015.
 */
public class Image {
    private static Context context;
    private static Image image;

    private Image() {
    }

    public static Image obj(Context cont) {
        context = cont;
        if (image == null)
            image = new Image();
        return image;
    }

    public void setImage(ImageView image, String url) {
        System.err.println("IMAGE URL " + VAR.IMAGE_URL + url);

        Picasso.with(context).load(VAR.IMAGE_URL + url).into(image);
//        Glide.with(context).load(VAR.IMAGE_URL+url).into(image);
    }

    public void setImage(ImageView image, String url, int err) {
        try {
            if (url == null) {
                image.setImageResource(err);

            } else if (url.isEmpty() || url.equals("null")) {
                image.setImageResource(err);
            } else {

                Log.i("IMAGE", VAR.IMAGE_URL + url);
                int radius = image.getHeight() / 2, margin = 5;
                Picasso.with(context).load(VAR.IMAGE_URL + url).transform(new RoundedTransformation(radius, margin)).into(image);

//                Picasso.with(context).load(VAR.IMAGE_URL + url).error(err).into(image);
            }

        } catch (Exception e) {
            e.printStackTrace();
            image.setImageResource(err);

        }

//        Glide.with(context).load(VAR.IMAGE_URL+url).placeholder(err).error(err).into(image);
    }

    public void setImage(final ImageView image, String url, final ProgressBar progressBar) {
        System.err.println("IMAGE URL " + VAR.IMAGE_URL + url);
        Picasso.with(context).load(VAR.IMAGE_URL + url).into(image, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError() {
                image.setImageResource(R.drawable.logomin);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
