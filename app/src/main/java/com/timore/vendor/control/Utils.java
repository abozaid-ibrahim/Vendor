package com.timore.vendor.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.views.EditPostActivity;
import com.timore.vendor.views.ProfileActivity;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;

/**
 * Created by usear on 11/30/2015.
 */
public class Utils {


    public static void uploadImage(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Pick image");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CameraImage.dispatchTakePictureIntent(activity);
            }
        });
        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CameraImage.openGallery(activity);
            }
        });
        builder.create().show();
    }

    public static void gotoUserProfile(Context context, long id) {
        Intent prof = new Intent(context, ProfileActivity.class);
        System.err.println("ID IS " + id);
        prof.putExtra(VAR.KEY_USER_ID, id);
        context.startActivity(prof);
    }


    public static void editPost(int position, Context context, Post post) {
        Intent prof = new Intent(context, EditPostActivity.class);
        prof.putExtra(VAR.KEY_POST, Parcels.wrap(post));

        context.startActivity(prof);
    }

    static public String bitMapToString(String filePath) {
        return bitMapToString(BitmapFactory.decodeFile(filePath));
    }
    static public String bitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.err.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.err.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.err.println("SIZE IS "+ bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = null;
        try {
            System.gc();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("EWN", "Out of memory error catched");
        }
        return temp;
    }
}
