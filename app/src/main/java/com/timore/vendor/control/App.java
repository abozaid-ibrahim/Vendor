package com.timore.vendor.control;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.timore.vendor.R;

import java.lang.reflect.Field;

/**
 * Created by usear on 11/26/2015.
 */
public class App extends Application {

    public static int userId;

    @Override
    public void onCreate() {
        super.onCreate();
        initial();
        setAppFont();
        //   Typeface xx=Typeface.createFromAsset(getAssets(),"");
    }

    private void initial() {
        userId = getSharedPreferences(VAR.PREF_NAME, 0).getInt(VAR.KEY_USER_ID, 0);
    }

    private void setAppFont() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/blippo.ttf");
        replaceFont("MONOSPACE", typeface);
        replaceFont("DEFAULT", typeface);
        replaceFont("SERIF", typeface);
        replaceFont("SANS_SERIF", typeface);
    }

    private void replaceFont(String s, Typeface font) {
        try {
            Field field = Typeface.class.getDeclaredField(s);
            field.setAccessible(true);
            field.set(null, font);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void print(String s) {
        System.err.println("==========================================");
        System.err.println("==========================================");
        System.err.println(s);
    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null)
            if (connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting())
                return true;
        return false;
    }

    public static void browseLink(Context context, String url) {
        if (url != null) {
            try {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browser);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context,context.getString(R.string.checkLink), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
