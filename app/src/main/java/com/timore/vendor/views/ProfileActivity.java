package com.timore.vendor.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;

import com.timore.vendor.R;
import com.timore.vendor.control.App;
import com.timore.vendor.control.SuperActivity;
import com.timore.vendor.control.VAR;
import com.timore.vendor.home.ProfileFragment;

public class ProfileActivity extends SuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        super.setToolBar(findViewById(R.id.toolbar), true);
        if (getIntent().getExtras() != null) {
            String userName = getIntent().getExtras().getString(VAR.KEY_USER_NAME);
            long userId = getIntent().getExtras().getLong(VAR.KEY_USER_ID);
            getSupportFragmentManager().beginTransaction().add(R.id.user_container,
                    ProfileFragment.getInstance(userId, userName, App.userId == userId)).commit();
        } else {
            if (!App.isConnected(this))
                Snackbar.make(findViewById(R.id.main_layout), getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

}
