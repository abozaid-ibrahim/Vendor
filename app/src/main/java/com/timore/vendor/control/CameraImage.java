package com.timore.vendor.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraImage {
    public static File photoFile;
    static String mCurrentPhotoPath;

    public static File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents

        return image;
    }


    public static void dispatchTakePictureIntent(Activity paramActivity) {
        Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (localIntent.resolveActivity(paramActivity.getPackageManager()) != null) {
            photoFile = createImageFile();
            if (photoFile != null) {
                localIntent.putExtra("output", Uri.fromFile(photoFile));
                paramActivity.startActivityForResult(localIntent, 132);
            }
        }
    }

    public static void dispatchTakePictureIntent(Fragment fragment) {
        Intent imageCapture = new Intent("android.media.action.IMAGE_CAPTURE");
        try {
            photoFile = createImageFile();
            if (photoFile != null) {
                imageCapture.putExtra("output", Uri.fromFile(photoFile));
                fragment.startActivityForResult(imageCapture, VAR.PICK_IAMGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void galleryAddPic(Context paramContext) {
        Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        localIntent.setData(Uri.fromFile(new File(mCurrentPhotoPath)));
        paramContext.sendBroadcast(localIntent);
    }

    public static Bitmap bitmapFromUri(Context context, Intent data) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void getImageFile(Context context, Intent data) {


        String[] arrayOfString = new String[1];
        arrayOfString[0] = "_data";
        Cursor cursor = context.getContentResolver().query(data.getData(), arrayOfString, null, null, null);
        assert (cursor != null);
        cursor.moveToFirst();
        photoFile = new File(cursor.getString(cursor.getColumnIndex(arrayOfString[0])));
        cursor.close();
    }

    public static void openGallery(Activity paramActivity) {
        paramActivity.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 13);
    }

    public static void openGallery(Fragment paramFragment) {
        paramFragment.startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 13);
    }

    public static void setPic(ImageView paramImageView) {
        int i = paramImageView.getWidth();
        int j = paramImageView.getHeight();
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, localOptions);
        int k = localOptions.outWidth;
        int m = localOptions.outHeight;
        i = Math.min(k / i, m / j);
        localOptions.inJustDecodeBounds = false;
        localOptions.inSampleSize = i;
        localOptions.inPurgeable = true;
        paramImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath, localOptions));
    }
}

