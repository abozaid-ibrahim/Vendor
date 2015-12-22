package com.timore.vendor.beanBojo;

import android.support.v4.app.Fragment;

/**
 * Created by usear on 11/26/2015.
 */
public class MainTab {
    Fragment fragment;
    int id;
    String title;
    int icon;

    public MainTab(Fragment fragment, int id, String title, int icon) {
        this.fragment = fragment;
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
