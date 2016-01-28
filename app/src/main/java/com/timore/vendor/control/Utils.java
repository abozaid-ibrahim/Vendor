package com.timore.vendor.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Base64;

import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.views.EditPostActivity;
import com.timore.vendor.views.ProfileActivity;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;

public class Utils {
    static public String bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        baos = new ByteArrayOutputStream();
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    public static String bitMapToString(String paramString) {
        return bitMapToString(BitmapFactory.decodeFile(paramString));
    }

    public static void editPost(int paramInt, Context paramContext, Post paramPost) {
        Intent localIntent = new Intent(paramContext, EditPostActivity.class);
        localIntent.putExtra("POST", Parcels.wrap(paramPost));
        paramContext.startActivity(localIntent);
    }

    public static void gotoUserProfile(Context paramContext, long paramLong) {
        Intent localIntent = new Intent(paramContext, ProfileActivity.class);
        System.err.println("ID IS " + paramLong);
        localIntent.putExtra(VAR.KEY_USER_ID, paramLong);
        paramContext.startActivity(localIntent);
    }

    public static void uploadImage(final Activity paramActivity) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramActivity);
        localBuilder.setTitle("Pick image");
        localBuilder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                CameraImage.dispatchTakePictureIntent(paramActivity);
            }
        });
        localBuilder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                CameraImage.openGallery(paramActivity);
            }
        });
        localBuilder.create().show();
    }

    public static void uploadImage(final Fragment paramFragment) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramFragment.getContext());
        localBuilder.setTitle("Pick image");

        localBuilder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                CameraImage.dispatchTakePictureIntent(paramFragment);
            }
        });
        localBuilder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                CameraImage.openGallery(paramFragment);
            }
        });
        localBuilder.create().show();
    }
}
