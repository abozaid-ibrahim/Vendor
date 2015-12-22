package com.timore.vendor.views.home;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.beanBojo.Profile;
import com.timore.vendor.control.App;
import com.timore.vendor.control.CameraImage;
import com.timore.vendor.control.Image;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.control.Utils;
import com.timore.vendor.control.VAR;
import com.timore.vendor.views.MainActivity;

import org.parceler.Parcels;

import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddPostFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    View layout;

    /*Views*/
    public ImageView imageView;
    Button submitBtn;
    Button uploadBtn;
    EditText subjectEt;
    EditText titleEt;
    private Post post;
    private TextView userName;
    private TextView label;
    protected View menuView;
    private TextView userDate;
    private ImageView userImage;

    public AddPostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_addpost, container, false);
        init();
        long id = App.userId;
        try {
            if (getArguments() != null) {
                post = Parcels.unwrap(getArguments().getParcelable(VAR.KEY_POST));
                titleEt.setText(post.getTitle());
                subjectEt.setText(post.getContent());
                label.setText(getString(R.string.Update));
                submitBtn.setText(getString(R.string.Update));
                Image.obj(getActivity()).setImage(imageView, post.getFile());
                id = post.getUser_id();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getUser(id);
        return layout;
    }

    private void init() {
        userImage = (ImageView) layout.findViewById(R.id.addpost_header_imageview);
        userDate = (TextView) layout.findViewById(R.id.addpost_header_date);
        userName = (TextView) layout.findViewById(R.id.addpost_header_username);
        menuView = layout.findViewById(R.id.addpost_header_option);
        imageView = (ImageView) layout.findViewById(R.id.upload_imageview);
        label = (TextView) layout.findViewById(R.id.addpost_lbl_addnewpost);
        titleEt = (EditText) layout.findViewById(R.id.addpost_et_title);
        subjectEt = (EditText) layout.findViewById(R.id.addpost_et_subject);
        submitBtn = (Button) layout.findViewById(R.id.addpost_btn_submit);
        uploadBtn = (Button) layout.findViewById(R.id.upload_image_button);

        submitBtn.setOnClickListener(this);
        uploadBtn.setText(getString(R.string.upload_image));
        uploadBtn.setOnClickListener(this);
        MainActivity.progressBar.setVisibility(View.GONE);

    }

    private void getUser(long userId) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().getUserProfile(userId, new Callback<Profile>() {
            @Override
            public void success(Profile profile, Response response) {
                Retrofit.res(profile + "", response);

                MainActivity.progressBar.setVisibility(View.GONE);
                updateUI(profile);
            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                MainActivity.progressBar.setVisibility(View.GONE);
                if (!App.isConnected(getActivity()))
                    Snackbar.make(layout, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void updateUI(Profile profile) {
        userName.setText(profile.getUsername());
        userDate.setText(profile.getDate_insert());
        Image.obj(getActivity()).setImage(userImage, profile.getImage(),R.drawable.galleryselected);

    }

    public void uploadPostImage(final View view) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);

        Retrofit.getInstance().uploadImage(CameraImage.photoFile, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                if (object != null) {
                    post(view, object.get("img_name").getAsString());
                } else {
                    if (!App.isConnected(getActivity()))
                        Snackbar.make(view, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                    MainActivity.progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(getActivity()))
                    Snackbar.make(view, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                MainActivity.progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void post(final View view, String imageName) {
        System.err.println("IMAGE NAME: " + imageName);
        Retrofit.getInstance().addPost(1, App.userId, titleEt.getText().toString(), imageName, new Random(100).nextInt() + "sdf.mp3"
                , subjectEt.getText().toString(), new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject object, Response response) {
                        Retrofit.res(object + "", response);
                        MainActivity.progressBar.setVisibility(View.GONE);
                        int id = Integer.valueOf(object.get("id").toString());
                        if (id > 0) {
                            titleEt.setText(null);
                            subjectEt.setText(null);
                            imageView.setImageResource(R.drawable.usericon);
                            Snackbar.make(view, getString(R.string.updated), Snackbar.LENGTH_LONG).show();
                        } else
                            Snackbar.make(view, getString(R.string.cant_update), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        MainActivity.progressBar.setVisibility(View.GONE);
                        if (!App.isConnected(getActivity()))
                            Snackbar.make(layout, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                    }
                });
    }

    @Override
    public void onDestroy() {
        CameraImage.photoFile = null;
        System.err.println("ON DESTROY FRAGMENT ADD POST");
        super.onDestroy();
    }

    private void update(final View view, String imageName) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().updatePost(1, post.getId(), App.userId, titleEt.getText().toString(), imageName
                , subjectEt.getText().toString(), new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject object, Response response) {
                        Retrofit.res(object + "", response);
                        MainActivity.progressBar.setVisibility(View.GONE);
                        int id = Integer.valueOf(object.get("sucess").toString());
                        if (id > 0) {
                            titleEt.setText(null);
                            subjectEt.setText(null);
                            imageView.setImageResource(R.drawable.usericon);
                            Snackbar.make(view, getString(R.string.updated), Snackbar.LENGTH_LONG).show();
                        } else
                            Snackbar.make(view, getString(R.string.cant_update), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        MainActivity.progressBar.setVisibility(View.GONE);
                        if (!App.isConnected(getActivity()))
                            Snackbar.make(layout, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addpost_btn_submit:
                if (validPost())
                    if (post != null && post.getId() > 0)
                        if (CameraImage.photoFile == null)
                            update(v, "");
                        else
                            updatePostImage(v);
                    else
                        uploadPostImage(v);
                else
                    Snackbar.make(v, getString(R.string.checkInputs), Snackbar.LENGTH_LONG).show();
                break;
            case R.id.upload_image_button:
                Utils.uploadImage(getActivity());
                break;
        }
    }

    private void updatePostImage(final View view) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);

        Retrofit.getInstance().uploadImage(CameraImage.photoFile, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                if (object != null) {
                    update(view,object.get("img_name").getAsString());
                } else {
                    if (!App.isConnected(getActivity()))
                        Snackbar.make(view, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                    MainActivity.progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(getActivity()))
                    Snackbar.make(view, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                MainActivity.progressBar.setVisibility(View.GONE);

            }
        });
    }

    private boolean validPost() {
        if (titleEt.getText().toString().isEmpty()) {
            titleEt.setError(getString(R.string.enter_field));
            return false;
        } else {
            titleEt.setError(null);
        }
        if (subjectEt.getText().toString().isEmpty()) {
            subjectEt.setError(getString(R.string.enter_field));
            return false;
        } else {
            subjectEt.setError(null);
        }
        return true;
    }


}
