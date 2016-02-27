package com.timore.vendor.control;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;

import com.timore.vendor.R;

/**
 * Created by Abuzeid on 2/5/2016
 */
public class NetworkLoading {

    private static ProgressDialog dialog;

    private NetworkLoading() {
    }

    public static void startLoading(Context context) {
        dialog = new ProgressDialog(context);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.setIndeterminate(false);
        dialog.setProgress(1);
        dialog.setMessage(context.getString(R.string.loading));


        dialog.show();
    }

    public static void stopLoading() {
        if (dialog != null)
            if (dialog.isShowing())
                dialog.dismiss();
    }
}
