package com.timore.vendor.views;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.beanBojo.Profile;
import com.timore.vendor.control.App;
import com.timore.vendor.control.CameraImage;
import com.timore.vendor.control.GPSTracker;
import com.timore.vendor.control.Image;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.control.SuperActivity;
import com.timore.vendor.control.Utils;
import com.timore.vendor.control.VAR;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditProfileActivity extends SuperActivity implements View.OnClickListener {
    /*Views*/

    @Bind(R.id.activity_progress)
    ProgressBar progressBar;
    @Bind(R.id.edit_btn_uploadimage)
    Button profileImgBtn;
    ImageView profileImageView;

    @Bind(R.id.edit_et_about)
    EditText aboutEt;
    @Bind(R.id.edit_et_email)
    EditText emailEt;
    @Bind(R.id.edit_et_facebook)
    EditText fbEt;
    @Bind(R.id.edit_et_hashtag)
    EditText hashtagEt;
    @Bind(R.id.edit_et_instagram)
    EditText instagramEt;
    @Bind(R.id.edit_et_website)
    EditText websiteEt;
    @Bind(R.id.edit_et_mobile)
    EditText mobileEt;
    @Bind(R.id.edit_et_twitter)
    EditText twitterEt;
    @Bind(R.id.edit_et_username)
    EditText usernameEt;
    @Bind(R.id.edit_btn_update)
    Button submit;
    @Bind(R.id.edit_btn_password)
    Button passBtn;


    @Bind(R.id.main_layout)
    View main;
    Profile profile;
    private GPSTracker gps;
    double lat = 0, longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        super.setToolBar(findViewById(R.id.toolbar), true);
        ButterKnife.bind(this);
        profileImageView = (ImageView) findViewById(R.id.edit_porfile_image);
        init();
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
        enableViews(false);
    }


    private void init() {
        profile = Parcels.unwrap(getIntent().getExtras().getParcelable(SuperActivity.PROFILE));
        if (profile != null) {
            usernameEt.setText(profile.getUsername());
            emailEt.setText(profile.getEmail());
            mobileEt.setText(profile.getMobile());
            aboutEt.setText(profile.getAbout());
            instagramEt.setText(profile.getInstagram());
            twitterEt.setText(profile.getTwitter());
            fbEt.setText(profile.getFacebook());
            System.err.println("prof image" + profile.getImage());
            if (profile.getImage() != null && profile.getImage().length() > 4)
                Image.obj(this).setImage(profileImageView, profile.getImage());
        }
        submit.setOnClickListener(this);
        passBtn.setOnClickListener(this);
        profileImgBtn.setOnClickListener(this);
    }

    public void uploadImage() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit.getInstance().uploadImage(CameraImage.photoFile, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                if (object != null) {
                    updateProfile(object.get("img_name").getAsString());
                } else {
                    if (!App.isConnected(EditProfileActivity.this))
                        Snackbar.make(main, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(EditProfileActivity.this))
                    Snackbar.make(main, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void updateProfile(String imgName) {
        System.err.println("IMAGE NAME: "+ imgName);
        Retrofit.getInstance().updateAccount(1, usernameEt.getText().toString(),
                mobileEt.getText().toString(), imgName
                , App.userId, aboutEt.getText().toString()
                , emailEt.getText().toString(), lat, longitude,
                mobileEt.getText().toString(), fbEt.getText().toString(),
                twitterEt.getText().toString(), instagramEt.getText().toString(),
                hashtagEt.getText().toString(), websiteEt.getText().toString(),
                new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject res, Response response) {
                        Retrofit.res(res + "", response);
                        progressBar.setVisibility(View.GONE);
                        String val = res.get("sucess").toString();
                        if (Integer.valueOf(val) > 0) {
                            Toast.makeText(EditProfileActivity.this, "Account updated", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Can't save updates", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        progressBar.setVisibility(View.GONE);
                        if (!App.isConnected(EditProfileActivity.this))
                            Snackbar.make(main, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                    }
                });
    }

    @Override
    protected void onDestroy() {
        CameraImage.photoFile=null;

        super.onDestroy();
        ButterKnife.unbind(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_btn_update:
                if (validInputs())
                    uploadImage();
                else
                    Snackbar.make(v, getString(R.string.checkInputs), Snackbar.LENGTH_LONG).show();
                break;

            case R.id.edit_btn_password:
                new ChangePasswordDialog(this).show();
                break;
            case R.id.edit_btn_uploadimage:
                Utils.uploadImage(this);
                break;

            default:

                break;
        }
    }

    private boolean validInputs() {

        if (usernameEt.getText().toString().isEmpty()) {
            usernameEt.setError(getString(R.string.enter_field));
            return false;
        } else {
            usernameEt.setError(null);
        }

        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        System.err.println("onSaveInstanceState");
        //outState.putParcelable("file_uri", Parcels.wrap(CameraImage.photoFile));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.err.println("onRestoreInstanceState");
        //CameraImage.photoFile = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.err.println("ON ACTIVITY RESULT");
        if (resultCode == RESULT_OK) {
            System.err.println("ON ACTIVITY RESULT RESULT_OK");

            if (requestCode == VAR.PICK_IAMGE) {
                System.err.println("ON ACTIVITY RESULT PICK_IAMGE");
                Uri selectedImage = data.getData();
                profileImageView.setImageURI(selectedImage);
                CameraImage.getImageFile(getBaseContext(), data);
                System.err.println("ON ACTIVITY RESULT done");
//                profileImageView.setImageResource(R.drawable.logo_2);

            } else if (requestCode == VAR.OPEN_CAMERA) {
                profileImageView.setImageBitmap(BitmapFactory.decodeFile(CameraImage.photoFile.getAbsolutePath()));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit:
                enableViews(true);
                return true;
            default:
                return false;
        }
    }

    private void enableViews(boolean enable) {

        main.setAlpha(enable ? 1 : 0.3f);

        usernameEt.setEnabled(enable);
        emailEt.setEnabled(enable);
        mobileEt.setEnabled(enable);
        hashtagEt.setEnabled(enable);
        fbEt.setEnabled(enable);
        instagramEt.setEnabled(enable);
        twitterEt.setEnabled(enable);
        websiteEt.setEnabled(enable);
        aboutEt.setEnabled(enable);
        passBtn.setEnabled(enable);
        submit.setEnabled(enable);
        profileImgBtn.setEnabled(enable);
    }


}
