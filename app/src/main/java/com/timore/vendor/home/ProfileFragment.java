package com.timore.vendor.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.adapters.PostsGridAdapter;
import com.timore.vendor.adapters.PostsRecyclerAdapter;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.beanBojo.Profile;
import com.timore.vendor.control.App;
import com.timore.vendor.control.Image;
import com.timore.vendor.control.NetworkLoading;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.control.SuperActivity;
import com.timore.vendor.control.VAR;
import com.timore.vendor.views.EditProfileActivity;
import com.timore.vendor.views.LoginActivity;
import com.timore.vendor.views.MainActivity;
import com.timore.vendor.views.MapsActivity;
import com.timore.vendor.views.PostActivity;

import org.parceler.Parcels;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static ProfileFragment instance;
    View layout;
    Button editProfile;
    /*Views*/
    private ImageView instagramView;
    private ImageView faceView;
    private ImageView profileImage;
    private ImageView twitterView;
    private ImageView listView;
    private ImageView mapIcon;
    private ImageView gridIcon;
    private TextView followersTv;
    private TextView followingTv;
    private TextView nameTv;
    private TextView imagesTv;
    private GridView gridView;
    private RecyclerView recyclerView;
    private Profile profile;
    private long userId;
    private boolean isMyProfile;
    private Button logoutButton;
    private TextView aboutTv;

    public static ProfileFragment getInstance(long id, String userName, boolean isMyProfile) {
//        if (instance == null) {
        instance = new ProfileFragment();
//        }
        Bundle bundle = new Bundle();
        bundle.putLong(VAR.KEY_USER_ID, id);
        bundle.putString(VAR.KEY_USER_NAME, userName);
        bundle.putBoolean(VAR.KEY_IS_MY_PROFILE, isMyProfile);
        instance.setArguments(bundle);
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getLong(VAR.KEY_USER_ID);
        String userName = getArguments().getString(VAR.KEY_USER_NAME);
        if (userName != null)
            getActivity().setTitle(userName);
        isMyProfile = getArguments().getBoolean(VAR.KEY_IS_MY_PROFILE);
        if (userId != App.userId)
            isMyProfile = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_profile, container, false);

        findViewById();
        init();
        getUser(userId);
        getUserPosts(userId);
        return layout;
    }

    private void findViewById() {
        aboutTv = (TextView) layout.findViewById(R.id.profile_tv_about);
        followersTv = (TextView) layout.findViewById(R.id.profile_tv_followersNum);
        followingTv = (TextView) layout.findViewById(R.id.profile_tv_followingNum);
        nameTv = (TextView) layout.findViewById(R.id.profile_tv_name);
        imagesTv = (TextView) layout.findViewById(R.id.profile_tv_imagesNum);
        gridView = (GridView) layout.findViewById(R.id.profile_gird);
        recyclerView = (RecyclerView) layout.findViewById(R.id.profile_recyclerView);
        profileImage = (ImageView) layout.findViewById(R.id.profile_iv);
        logoutButton = (Button) layout.findViewById(R.id.profile_logout);

        editProfile = (Button) layout.findViewById(R.id.profile_edit);
        gridIcon = (ImageView) layout.findViewById(R.id.profile_grid_icon);
        instagramView = (ImageView) layout.findViewById(R.id.profile_instagram);
        faceView = (ImageView) layout.findViewById(R.id.profile_facebook);
        twitterView = (ImageView) layout.findViewById(R.id.profile_iv_twitter);
        listView = (ImageView) layout.findViewById(R.id.profile_listIcon);
        mapIcon = (ImageView) layout.findViewById(R.id.profile_map);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        editProfile.setOnClickListener(this);
        instagramView.setOnClickListener(this);
        twitterView.setOnClickListener(this);
        faceView.setOnClickListener(this);
        listView.setOnClickListener(this);
        gridIcon.setOnClickListener(this);
        mapIcon.setOnClickListener(this);

        logoutButton.setOnClickListener(this);
        if (isMyProfile) {
            Log.i("prof", "this is my profile");
            logoutButton.setText(getString(R.string.logout));
            editProfile.setText(getString(R.string.edit_profile));
            editProfile.setVisibility(View.VISIBLE);
        } else {
            logoutButton.setText(getString(R.string.follow));
            editProfile.setVisibility(View.GONE);
            Log.i("prof", "this is NOT my profile");

        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent postIntent = new Intent(getActivity(), PostActivity.class);
                postIntent.putExtra(PostActivity.POST, Parcels.wrap(gridView.getAdapter().getItem(position)));
                getActivity().startActivity(postIntent);
            }
        });
    }

    private void getUser(long userId) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().getUserProfile(userId, App.userId, new Callback<Profile>() {
            @Override
            public void success(Profile profile, Response response) {
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


    private void getUserPosts(long userId) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().getUserPosts(userId, new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                Retrofit.res(posts + "", response);
                MainActivity.progressBar.setVisibility(View.GONE);
                if (posts != null) {
                    recyclerView.setAdapter(new PostsRecyclerAdapter(getActivity(), posts));
                    gridView.setAdapter(new PostsGridAdapter(getActivity(), posts));
                }

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

    private void updateUI(Profile myProfile) {
        if (myProfile != null) {
            profile = myProfile;
            if (!isMyProfile) {
                logoutButton.setText(profile.is_follow() ? getString(R.string.unfollow) : getString(R.string.follow));
            } else {
                logoutButton.setText(getString(R.string.logout));

            }
            followersTv.setText(String.valueOf(profile.getFollowing()));
            followingTv.setText(String.valueOf(profile.getFollower()));
            Log.e("client", "posts count " + profile.getPosts());
            imagesTv.setText(String.valueOf(profile.getPosts()));
            nameTv.setText(profile.getUsername());
            aboutTv.setText(profile.getAbout());
            Image.obj(getActivity()).setImage(profileImage, profile.getImage(), R.drawable.usericon);

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_edit:
                if (isMyProfile) {
                    if (profile != null) {
                        Intent edit = new Intent(getActivity(), EditProfileActivity.class);
                        edit.putExtra(SuperActivity.PROFILE, Parcels.wrap(profile));
                        startActivity(edit);
                    } else {
                        System.err.println("????? PROFILE OBJECT IS NULL");
                    }
                } else {

                    //message
                }
                break;
            case R.id.profile_logout:
                if (profile != null)
                if (isMyProfile) {
                    showLogoutAlert();
                } else {
                    followUnFollowUser();
                }
                break;
            case R.id.profile_iv_twitter:
                if (profile != null)
                    App.browseLink(getActivity(), profile.getTwitter());

                break;
            case R.id.profile_instagram:
                if (profile != null)
                    App.browseLink(getActivity(), profile.getInstagram());

                break;
            case R.id.profile_map:
                if (profile != null) {
                    Intent map = new Intent(getActivity(), MapsActivity.class);
                    map.putExtra(VAR.KEY_USER_NAME, profile.getUsername());
                    map.putExtra(VAR.LATITUDE, profile.getLatitude());
                    map.putExtra(VAR.LONGITUDE, profile.getLongitude());

                    startActivity(map);
                }
                break;
            case R.id.profile_listIcon:
                gridView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case R.id.profile_grid_icon:
                recyclerView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);

                break;
            default:
                break;
        }
    }

    private void showLogoutAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.logout)).setMessage(getString(R.string.logout_now))
                .setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getActivity().getSharedPreferences(VAR.PREF_NAME, 0).edit().clear().commit();
                        dialog.dismiss();
                        Intent loginAct = new Intent(getActivity(), LoginActivity.class);
                        loginAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        loginAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(loginAct);

//                                    getActivity().finish();

                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();

    }

    private void followUnFollowUser() {


        if (profile.is_follow()) {
            NetworkLoading.startLoading(getActivity());
            Retrofit.getInstance().followUser(1, App.userId, profile.getId(), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    NetworkLoading.stopLoading();

                    profile.setIs_follow(false);
                    logoutButton.setText(getString(R.string.unfollow));
                }

                @Override
                public void failure(RetrofitError error) {
                    NetworkLoading.stopLoading();

                    if (!App.isConnected(getActivity()))
                        Snackbar.make(layout, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                }
            });

        } else {
            NetworkLoading.startLoading(getActivity());

            Retrofit.getInstance().unFollowUser(App.userId, profile.getId(), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {

                    profile.setIs_follow(true);
                    logoutButton.setText(getString(R.string.follow));
                    NetworkLoading.stopLoading();
                }

                @Override
                public void failure(RetrofitError error) {
                    NetworkLoading.stopLoading();

                    if (!App.isConnected(getActivity()))
                        Snackbar.make(layout, getString(R.string.no_net), Snackbar.LENGTH_LONG).show();

                }
            });


        }
    }
}
