package com.timore.vendor.beanBojo;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * Created by Abuzeid on 1/28/2016.
 */
public class PostImage {
    String path;
    File file;
    Bitmap bitmap;
    Uri uri;

    public PostImage(String path, File file, Bitmap bitmap, Uri uri) {
        this.path = path;
        this.file = file;
        this.bitmap = bitmap;
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
