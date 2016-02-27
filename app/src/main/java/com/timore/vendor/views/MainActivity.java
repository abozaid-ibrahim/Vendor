package com.timore.vendor.views;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.timore.vendor.R;
import com.timore.vendor.adapters.MainViewPagerAdapter;
import com.timore.vendor.beanBojo.MainTab;
import com.timore.vendor.control.App;
import com.timore.vendor.home.AddPostFragment;
import com.timore.vendor.home.MainFragment;
import com.timore.vendor.home.NotificationFragment;
import com.timore.vendor.home.ProfileFragment;
import com.timore.vendor.home.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ProgressBar progressBar;
    private static NotificationFragment instance;
    TabLayout tabLayout;
    ViewPager mViewPager;
    List<MainTab> mFragmentList;

    public static NotificationFragment getInstance() {
        if (instance == null) {
            instance = new NotificationFragment();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentList = new ArrayList<>();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.container);
        progressBar = (ProgressBar) findViewById(R.id.activity_progress);
        setupViewPager(mViewPager);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void setupViewPager(ViewPager viewPager) {
        mFragmentList.add(new MainTab(ProfileFragment.getInstance(App.userId, null, true), 0, "", R.drawable.profile));
        mFragmentList.add(new MainTab(NotificationFragment.getInstance(), 1, "", R.drawable.notification));

        mFragmentList.add(new MainTab(AddPostFragment.getInstance(), 2, "", R.drawable.gallery));
        mFragmentList.add(new MainTab(SearchFragment.getInstance(), 3, "", R.drawable.search));
        mFragmentList.add(new MainTab(MainFragment.getInstance(), 4, "", R.drawable.home));
        PagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < mFragmentList.size(); i++) {
            TabLayout.Tab xx = tabLayout.getTabAt(i);
            assert xx != null;
            xx.setIcon(mFragmentList.get(i).getIcon());
        }

        viewPager.setCurrentItem(4);
    }


}
