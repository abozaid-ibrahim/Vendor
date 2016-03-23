package com.timore.vendor.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.beanBojo.PostImage;
import com.timore.vendor.beanBojo.Profile;
import com.timore.vendor.control.App;
import com.timore.vendor.control.CameraImage;
import com.timore.vendor.control.Image;
import com.timore.vendor.control.NetworkLoading;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.control.Utils;
import com.timore.vendor.control.VAR;
import com.timore.vendor.views.MainActivity;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddPostFragment extends Fragment implements OnClickListener {
    private static AddPostFragment instance;
    protected View menuView;
    View layout;
    EditText subjectEt;
    Button submitBtn;
    EditText titleEt;
    Button uploadBtn;
    private TextView label;
    private Post post;
    private TextView userDate;
    private ImageView userImage;
    private TextView userName;
    //    private PostImagesAdapter adapter;
    private List<PostImage> postImagesArray;
    private long userId;
    private ImageView postImageView;

    public static AddPostFragment getInstance() {
        if (instance == null) {
            instance = new AddPostFragment();
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_addpost, container, false);
        findViewById();
        canUpdatePost();
        getUser(userId);
        return layout;
    }


    private void getUser(long userId) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().getUserProfile(userId, App.userId, new Callback<Profile>() {
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

    private void clearFields() {
        titleEt.setText(null);
        subjectEt.setText(null);
        postImagesArray.clear();
        CameraImage.photoFile = null;
        postImageView.setImageBitmap(null);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void updatePost(View view) {
        if (postImagesArray.size() == 0) {
            updatePost(view, null, post.getId(), titleEt.getText().toString(),
                    subjectEt.getText().toString());
        } else {
            updatePostImage(view, post.getId(), titleEt.getText().toString(),
                    subjectEt.getText().toString(), postImagesArray.get(0).getPath());
        }
    }

    private void canUpdatePost() {
        userId = App.userId;
        try {
            if (getArguments() != null) {
                post = Parcels.unwrap(getArguments().getParcelable(VAR.KEY_POST));
                titleEt.setText(post.getTitle());
                subjectEt.setText(post.getContent());
                label.setText(getString(R.string.Update));
                submitBtn.setText(getString(R.string.Update));
                Image.obj(getActivity()).setImage(userImage, post.getFile(), R.drawable.user);
                userId = post.getUser_id();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        if (CameraImage.photoFile != null) {
            paramBundle.putString("FILEPATH", CameraImage.photoFile.getAbsolutePath());
        }
    }


    public void onViewStateRestored(Bundle paramBundle) {
        super.onViewStateRestored(paramBundle);
        try {
            if (paramBundle != null) {
                if ((paramBundle.containsKey("FILEPATH")) & (paramBundle.getString("FILEPATH") != null)) {
                    CameraImage.photoFile = new File(paramBundle.getString("FILEPATH", ""));
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void findViewById() {
        userImage = (ImageView) layout.findViewById(R.id.addpost_header_imageview);
        userDate = (TextView) layout.findViewById(R.id.addpost_header_date);
        userName = (TextView) layout.findViewById(R.id.addpost_header_username);
        menuView = layout.findViewById(R.id.addpost_header_option);


        label = (TextView) layout.findViewById(R.id.addpost_lbl_addnewpost);
        titleEt = (EditText) layout.findViewById(R.id.addpost_et_title);
        subjectEt = (EditText) layout.findViewById(R.id.addpost_et_subject);
        submitBtn = (Button) layout.findViewById(R.id.addpost_btn_submit);
        uploadBtn = (Button) layout.findViewById(R.id.upload_image_button);

        submitBtn.setOnClickListener(this);
        uploadBtn.setText(getString(R.string.upload_image));
        uploadBtn.setOnClickListener(this);
        MainActivity.progressBar.setVisibility(View.GONE);

        RecyclerView imagesRecyclerView = (RecyclerView) layout.findViewById(R.id.addpost_images_recycler);
        postImageView = (ImageView) layout.findViewById(R.id.addpost_post_image);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        postImagesArray = new ArrayList<>(1);
//        adapter = new PostImagesAdapter(getActivity(), postImagesArray);
//        imagesRecyclerView.setAdapter(adapter);

        layout.findViewById(R.id.addpost_header_option).setVisibility(View.GONE);
    }


    private void updateUI(Profile prof) {
        if (prof != null) {
            this.userName.setText(prof.getUsername());
            this.userDate.setText(prof.getDate_insert());
            Image.obj(getActivity()).setImage(this.userImage, prof.getImage(), R.drawable.user);
        }
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.err.println("ON ACTIVITY RESULT");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == VAR.PICK_IAMGE) {
                Uri selectedImage = data.getData();
                CameraImage.getImageFile(getContext(), data);
                postImagesArray.add(0, new PostImage(CameraImage.photoFile.getPath(), null, null, selectedImage));
//                adapter.notifyItemInserted(adapter.getItemCount());
                postImageView.setImageBitmap(BitmapFactory.decodeFile(postImagesArray.get(0).getPath()));
            } else if (requestCode == VAR.OPEN_CAMERA) {
//                this.imageView.setImageBitmap(BitmapFactory.decodeFile(CameraImage.photoFile.getAbsolutePath()));
//                addPostFragment.imageView.setImageBitmap(Utils.getCapturedImage(data));
                postImagesArray.add(0, new PostImage(CameraImage.photoFile.getPath(), null, null, null));
//                adapter.notifyItemInserted(adapter.getItemCount());
                postImageView.setImageBitmap(BitmapFactory.decodeFile(postImagesArray.get(0).getPath()));

            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addpost_btn_submit:
                if (validPost()) {
                    NetworkLoading.startLoading(getActivity());
                    if (post != null && post.getId() > 0) {
                        updatePost(v);
                    } else {
                        String title = titleEt.getText().toString();
                        String subject = subjectEt.getText().toString();
                        if (postImagesArray.size() > 0) {
                            uploadPostImage(v, title, subject,
                                    postImagesArray.get(0).getPath());
                        } else {
                            Snackbar.make(v, getString(R.string.upload_image_message), Snackbar.LENGTH_LONG).show();


                        }

                    }
                } else
                    Snackbar.make(v, getString(R.string.checkInputs), Snackbar.LENGTH_LONG).show();
                break;
            case R.id.upload_image_button:
            case R.id.upload_imageview:
                Utils.uploadImage(this);


                break;
        }
    }

    public void updatePostImage(final View view, final long postId, final String title, final String subject, String path) {

        Retrofit.getInstance().uploadImage(Utils.bitMapToString(path), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                if (object != null) {
                    updatePost(view, object.get("img_name").getAsString(), postId, title, subject);
                } else {
                    if (!App.isConnected(view.getContext()))
                        Snackbar.make(view, view.getContext().getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                    NetworkLoading.stopLoading();

                }
            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(view.getContext()))
                    Snackbar.make(view, view.getContext().getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                NetworkLoading.stopLoading();


            }
        });
    }

    public void addPost(final View view, String imageName, String title, String subject) {
        Retrofit.getInstance().addPost(1, App.userId, title, imageName, new Random(100).nextInt() + "sdf.mp3"
                , subject, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject object, Response response) {
                        Retrofit.res(object + "", response);
                        NetworkLoading.stopLoading();
                        int id = Integer.valueOf(object.get("id").toString());
                        if (id > 0) {

                            clearFields();
                            Snackbar.make(view, view.getContext().getString(R.string.done), Snackbar.LENGTH_LONG).show();
                        } else
                            Snackbar.make(view, view.getContext().getString(R.string.cant_post), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        NetworkLoading.stopLoading();
                        if (!App.isConnected(view.getContext()))
                            Snackbar.make(view, view.getContext().getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                    }
                });
    }

    public void updatePost(final View view, String imageName, long postId, String title, String subject) {
        Retrofit.getInstance().updatePost(1, postId, App.userId, title, imageName
                , subject, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject object, Response response) {
                        Retrofit.res(object + "", response);
                        NetworkLoading.stopLoading();
                        int id = Integer.valueOf(object.get("sucess").toString());
                        if (id > 0) {
                            Snackbar.make(view, view.getContext().getString(R.string.updated), Snackbar.LENGTH_LONG).show();
                        } else
                            Snackbar.make(view, view.getContext().getString(R.string.cant_update), Snackbar.LENGTH_LONG).show();
                        clearFields();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Retrofit.failure(error);
                        NetworkLoading.stopLoading();
                        if (!App.isConnected(view.getContext()))
                            Snackbar.make(view, view.getContext().getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                    }
                });
    }

    public void uploadPostImage(final View view, final String title, final String subject, String path) {

        Retrofit.getInstance().uploadImage(Utils.bitMapToString(path), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                if (object != null) {
                    addPost(view, object.get("img_name").getAsString(), title, subject);
                } else {
                    if (!App.isConnected(view.getContext()))
                        Snackbar.make(view, view.getContext().getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                    NetworkLoading.stopLoading();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(view.getContext()))
                    Snackbar.make(view, view.getContext().getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                NetworkLoading.stopLoading();

            }
        });
    }

}

