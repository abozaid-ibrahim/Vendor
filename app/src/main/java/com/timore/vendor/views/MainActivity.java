package com.timore.vendor.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.timore.vendor.control.CameraImage;
import com.timore.vendor.control.VAR;
import com.timore.vendor.views.home.AddPostFragment;
import com.timore.vendor.views.home.MainFragment;
import com.timore.vendor.views.home.NotificationFragment;
import com.timore.vendor.views.home.ProfileFragment;
import com.timore.vendor.views.home.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.container)
    ViewPager mViewPager;
    public static ProgressBar progressBar;

    List<MainTab> mFragmentList;
    private AddPostFragment addPostFragment;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFragmentList = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.activity_progress);
        setupViewPager(mViewPager);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }

    private void setupViewPager(ViewPager viewPager) {
        addPostFragment = new AddPostFragment();
        mFragmentList.add(new MainTab(ProfileFragment.getInstance(App.userId), 0, getString(R.string.hello_blank_fragment), R.drawable.profile));
        mFragmentList.add(new MainTab(new NotificationFragment(), 1, getString(R.string.hello_blank_fragment), R.drawable.notification));

        mFragmentList.add(new MainTab(addPostFragment, 2, getString(R.string.hello_blank_fragment), R.drawable.gallery));
        mFragmentList.add(new MainTab(new SearchFragment(), 3, getString(R.string.hello_blank_fragment), R.drawable.search));
        mFragmentList.add(new MainTab(new MainFragment(), 4, getString(R.string.hello_blank_fragment), R.drawable.home));
        PagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < mFragmentList.size(); i++) {
            if (tabLayout.getTabAt(i) != null & mFragmentList.get(i) != null) {
                tabLayout.getTabAt(i).setIcon(mFragmentList.get(i).getIcon());
            }

        }
        viewPager.setCurrentItem(4);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        System.err.println("ON ACTIVITY RESULT");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == VAR.PICK_IAMGE) {
                Uri selectedImage = data.getData();
                addPostFragment.imageView.setImageURI(selectedImage);
                CameraImage.getImageFile(getBaseContext(), data);

            } else if (requestCode == VAR.OPEN_CAMERA) {
                try {
                    addPostFragment.imageView.setImageBitmap(BitmapFactory.decodeFile(CameraImage.photoFile.getAbsolutePath()));
                } catch (Exception e) {
                    e.printStackTrace();
                    CameraImage.setPic(addPostFragment.imageView);

                }
//                addPostFragment.imageView.setImageBitmap(Utils.getCapturedImage(data));
            }
        }
    }

}
