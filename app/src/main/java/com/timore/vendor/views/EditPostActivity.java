package com.timore.vendor.views;

import android.os.Bundle;
import android.view.MenuItem;

import com.timore.vendor.R;
import com.timore.vendor.control.SuperActivity;
import com.timore.vendor.control.VAR;
import com.timore.vendor.home.AddPostFragment;

public class EditPostActivity extends SuperActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        super.setToolBar(findViewById(R.id.toolbar), true);
        if (getIntent().getExtras() != null) {
            AddPostFragment fragment = new AddPostFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(VAR.KEY_POST, getIntent().getExtras().getParcelable(VAR.KEY_POST));
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.editpost_container, fragment).commit();
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
