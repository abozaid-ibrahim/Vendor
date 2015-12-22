package com.timore.vendor.control;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by usear on 11/30/2015.
 */
public class Image {
    private static Context context;

    private Image() {
    }

    private static Image image;

    public static Image obj(Context cont) {
        context = cont;
        if (image == null)
            image = new Image();
        return image;
    }

    public void setImage(ImageView image, String url) {
        System.err.println("IMAGE URL "+VAR.IMAGE_URL+url);

        Glide.with(context).load(VAR.IMAGE_URL+url).into(image);
    }

    public void setImage(ImageView image, String url, int err) {
        System.err.println("IMAGE URL "+VAR.IMAGE_URL+url);
        Glide.with(context).load(VAR.IMAGE_URL+url).placeholder(err).error(err).into(image);
    }

    public void setImage(final ImageView image, String url, final ProgressBar progressBar) {
        System.err.println("IMAGE URL "+VAR.IMAGE_URL+url);

        Glide.with(context).load(VAR.IMAGE_URL+url).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                System.err.println("GLIDE>>>>>>onException");

                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                System.err.println("GLIDE>>>>>>onResourceReady");
//                image.setImageDrawable(resource);
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(image);
    }

}
