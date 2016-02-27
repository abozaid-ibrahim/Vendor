package com.timore.vendor.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.beanBojo.Profile;
import com.timore.vendor.control.App;
import com.timore.vendor.control.CameraImage;
import com.timore.vendor.control.GPSTracker;
import com.timore.vendor.control.Image;
import com.timore.vendor.control.NetworkLoading;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.control.SuperActivity;
import com.timore.vendor.control.Utils;
import com.timore.vendor.control.VAR;

import org.parceler.Parcels;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditProfileActivity extends SuperActivity implements View.OnClickListener {
    /*Views*/

    final String KEY_IMAGE_PATH = "FILEPATH`";
    Button profileImgBtn;
    ImageView profileImageView;
    EditText aboutEt;
    EditText emailEt;
    EditText fbEt;
    EditText hashtagEt;
    EditText instagramEt;
    EditText websiteEt;
    EditText mobileEt;
    EditText twitterEt;
    EditText usernameEt;
    Button submit;
    Button passBtn;
    View main;
    Profile profile;
    double lat = 0, longitude = 0;
    private boolean imageChanged;
    private Bitmap uploadedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        super.setToolBar(findViewById(R.id.toolbar), true);
        findViewById();
        init();

    }

    public void findViewById() {
        profileImgBtn = (Button) findViewById(R.id.edit_btn_uploadimage);
        main = findViewById(R.id.main_layout);
        passBtn = (Button) findViewById(R.id.edit_btn_password);
        submit = (Button) findViewById(R.id.edit_btn_update);
        usernameEt = (EditText) findViewById(R.id.edit_et_username);
        twitterEt = (EditText) findViewById(R.id.edit_et_twitter);
        mobileEt = (EditText) findViewById(R.id.edit_et_mobile);
        aboutEt = (EditText) findViewById(R.id.edit_et_about);

        emailEt = (EditText) findViewById(R.id.edit_et_email);
        fbEt = (EditText) findViewById(R.id.edit_et_facebook);
        hashtagEt = (EditText) findViewById(R.id.edit_et_hashtag);
        instagramEt = (EditText) findViewById(R.id.edit_et_instagram);
        websiteEt = (EditText) findViewById(R.id.edit_et_website);
        profileImageView = (ImageView) findViewById(R.id.edit_porfile_image);

    }

    private void init() {
        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
//        enableViews(true);
        profile = Parcels.unwrap(getIntent().getExtras().getParcelable(SuperActivity.PROFILE));
        if (profile != null) {
            usernameEt.setText(profile.getUsername());
            emailEt.setText(profile.getEmail());
            mobileEt.setText(profile.getMobile());
            aboutEt.setText(profile.getAbout());
            instagramEt.setText(profile.getInstagram());
            twitterEt.setText(profile.getTwitter());
            fbEt.setText(profile.getFacebook());
            Log.i("editprofile", "prof image >> " + profile.getImage());
            if (profile.getImage() != null && profile.getImage().length() > 4)
                Image.obj(this).setImage(profileImageView, profile.getImage());
        }
        submit.setOnClickListener(this);
        passBtn.setOnClickListener(this);
        profileImgBtn.setOnClickListener(this);
        profileImageView.setOnClickListener(this);
    }

    public void uploadImage() {
        NetworkLoading.startLoading(this);


        Retrofit.getInstance().uploadImage(
                Utils.bitMapToString(uploadedBitmap),
                new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject object, Response response) {
                        if (object != null) {
                            updateProfile(object.get("img_name").getAsString());
                        } else {
                            if (!App.isConnected(EditProfileActivity.this))
                                Snackbar.make(main, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                            NetworkLoading.stopLoading();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        if (!App.isConnected(EditProfileActivity.this))
                            Snackbar.make(main, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                        NetworkLoading.stopLoading();
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (CameraImage.photoFile != null)
            outState.putString(KEY_IMAGE_PATH, CameraImage.photoFile.getAbsolutePath());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            if (savedInstanceState != null)
                if (savedInstanceState.containsKey(KEY_IMAGE_PATH)) {
                    if (savedInstanceState.getString(KEY_IMAGE_PATH) != null)
                        CameraImage.photoFile = new File(savedInstanceState.getString(KEY_IMAGE_PATH));
                }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void updateProfile(String imgName) {
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
                        NetworkLoading.stopLoading();
                        String val = res.get("sucess").toString();
                        if (Integer.valueOf(val) > 0) {
                            Toast.makeText(EditProfileActivity.this, getString(R.string.account_updated), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditProfileActivity.this, getString(R.string.cant_update_account), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        NetworkLoading.stopLoading();
                        if (!App.isConnected(EditProfileActivity.this))
                            Snackbar.make(main, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                    }
                });
    }

    @Override
    protected void onDestroy() {
        CameraImage.photoFile = null;

        super.onDestroy();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_btn_update:
                if (validInputs()) {
                    if (imageChanged) {
                        Log.i("editprofile", "update profile image");
                        uploadImage();
                    } else {
                        updateProfile(null);
                    }

                } else
                    Snackbar.make(v, getString(R.string.checkInputs), Snackbar.LENGTH_LONG).show();
                break;

            case R.id.edit_btn_password:
                new ChangePasswordDialog(this).show();
                break;
            case R.id.edit_porfile_image:
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageChanged = true;
            if (requestCode == VAR.PICK_IAMGE) {
                Uri selectedImage = data.getData();
                uploadedBitmap = CameraImage.bitmapFromUri(EditProfileActivity.this, data);
                profileImageView.setImageBitmap(uploadedBitmap);
//                profileImageView.setImageURI(selectedImage);

            } else if (requestCode == VAR.OPEN_CAMERA) {
                uploadedBitmap = BitmapFactory.decodeFile(CameraImage.photoFile.getAbsolutePath());
                profileImageView.setImageBitmap(uploadedBitmap);
            }
        }
    }

    /*

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

    */
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
