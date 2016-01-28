package com.timore.vendor.views.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.timore.vendor.adapters.PostImagesAdapter;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.beanBojo.PostImage;
import com.timore.vendor.beanBojo.Profile;
import com.timore.vendor.control.App;
import com.timore.vendor.control.CameraImage;
import com.timore.vendor.control.Image;
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
    final String KEY_IMAGE_PATH = "FILEPATH";
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
    private RecyclerView imagesRecyclerView;
    private List<PostImage> postImagesArray;
    private PostImagesAdapter adapter;

    private void enableView(View paramView, boolean paramBoolean) {
        paramView.setClickable(paramBoolean);
        paramView.setEnabled(paramBoolean);
    }

    private void getUser(long paramLong) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().getUserProfile(paramLong, new Callback<Profile>() {
            public void failure(RetrofitError paramAnonymousRetrofitError) {
                Retrofit.failure(paramAnonymousRetrofitError);
                MainActivity.progressBar.setVisibility(View.GONE);
                if (!App.isConnected(AddPostFragment.this.getActivity())) {
                    Snackbar.make(layout, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();
                }
            }

            public void success(Profile paramAnonymousProfile, Response paramAnonymousResponse) {
                Retrofit.res(paramAnonymousProfile + "", paramAnonymousResponse);
                MainActivity.progressBar.setVisibility(View.GONE);
                AddPostFragment.this.updateUI(paramAnonymousProfile);
            }
        });
    }

    private void init() {
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

        imagesRecyclerView = (RecyclerView) layout.findViewById(R.id.addpost_images_recycler);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        postImagesArray = new ArrayList<>();
        adapter = new PostImagesAdapter(getActivity(), postImagesArray);
        imagesRecyclerView.setAdapter(adapter);
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
//                            adapt.clear;
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
//                            clear adat
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


    private void updatePostImage(final View view) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);

        Retrofit.getInstance().uploadImage(Utils.bitMapToString(CameraImage.photoFile.getAbsolutePath()), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);
                if (object != null) {
                    update(view, object.get("img_name").getAsString());
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

    private void updateUI(Profile paramProfile) {
        this.userName.setText(paramProfile.getUsername());
        this.userDate.setText(paramProfile.getDate_insert());
        Image.obj(getActivity()).setImage(this.userImage, paramProfile.getImage(), 2130837662);
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
                postImagesArray.add(new PostImage(CameraImage.photoFile.getPath(), null, null, selectedImage));
                adapter.notifyItemInserted(adapter.getItemCount());

            } else if (requestCode == VAR.OPEN_CAMERA) {
//                this.imageView.setImageBitmap(BitmapFactory.decodeFile(CameraImage.photoFile.getAbsolutePath()));
//                addPostFragment.imageView.setImageBitmap(Utils.getCapturedImage(data));
                postImagesArray.add(new PostImage(CameraImage.photoFile.getPath(), null, null, null));
                adapter.notifyItemInserted(adapter.getItemCount());
            }
        }
    }


    public void onAttach(Context paramContext) {
        super.onAttach(paramContext);
        System.err.println("####### ADDPOST FRAGMENT onAttach  ");
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
            case R.id.upload_imageview:
                Utils.uploadImage(getActivity());
                break;
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
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
//                Image.obj(getActivity()).setImage(imageView, post.getFile());
                id = post.getUser_id();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getUser(id);
        return layout;
    }

    public void onDestroy() {
        super.onDestroy();
        System.err.println("####### ADDPOST FRAGMENT onDestroy  ");
    }

    public void onDestroyView() {
        super.onDestroyView();
        System.err.println("####### ADDPOST FRAGMENT onDestroyView  ");
    }

    public void onDetach() {
        super.onDetach();
        System.err.println("####### ADDPOST FRAGMENT onDetach  ");
    }

    public void onResume() {
        super.onResume();
        System.err.println("####### ADDPOST FRAGMENT onResume  ");
    }

    public void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        System.err.println("===========onSaveInstanceState=====================");
        if (CameraImage.photoFile != null) {
            paramBundle.putString("FILEPATH", CameraImage.photoFile.getAbsolutePath());
        }
    }

    public void onStart() {
        super.onStart();
        System.err.println("####### ADDPOST FRAGMENT onStart  ");
    }

    public void onViewStateRestored(Bundle paramBundle) {
        super.onViewStateRestored(paramBundle);
        System.err.println("===========onRestoreInstanceState====================");
        try {
            if ((paramBundle.containsKey("FILEPATH")) && (paramBundle.getString("FILEPATH") != null)) {
                CameraImage.photoFile = new File(paramBundle.getString("FILEPATH", ""));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void uploadPostImage(final View view) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);

        Retrofit.getInstance().uploadImage(Utils.bitMapToString(CameraImage.photoFile.getAbsolutePath()), new Callback<JsonObject>() {
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

}

