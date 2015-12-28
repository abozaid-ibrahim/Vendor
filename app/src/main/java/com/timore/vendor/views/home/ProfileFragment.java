package com.timore.vendor.views.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.timore.vendor.R;
import com.timore.vendor.adapters.PostsGridAdapter;
import com.timore.vendor.adapters.PostsRecyclerAdapter;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.beanBojo.Profile;
import com.timore.vendor.control.App;
import com.timore.vendor.control.Image;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProfileFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static ProfileFragment instance;
    private long userId;
    View layout;

    /*Views*/

    @Bind(R.id.profile_edit)
    Button editProfile;
    @Bind(R.id.profile_instagram)
    ImageView instagramView;
    @Bind(R.id.profile_facebook)
    ImageView faceView;
    @Bind(R.id.profile_iv)
    ImageView profileImage;
    @Bind(R.id.profile_iv_twitter)
    ImageView twitterView;
    @Bind(R.id.profile_listIcon)
    ImageView listView;
    @Bind(R.id.profile_map)
    ImageView mapIcon;
    @Bind(R.id.profile_grid_icon)
    ImageView gridIcon;
    @Bind(R.id.profile_tv_followersNum)
    TextView followersTv;
    @Bind(R.id.profile_tv_followingNum)
    TextView followingTv;
    @Bind(R.id.profile_tv_name)
    TextView nameTv;
    @Bind(R.id.profile_tv_imagesNum)
    TextView imagesTv;
    @Bind(R.id.profile_gird)
    GridView gridView;
    @Bind(R.id.profile_recyclerView)
    RecyclerView recyclerView;
    Profile profile;
    private boolean isMyProfile;
    private Button logoutButton;

    public ProfileFragment() {
    }

    public static ProfileFragment getInstance(long id, boolean isMyProfile) {
//        if (instance == null) {
        instance = new ProfileFragment();
//        }
        Bundle bundle = new Bundle();
        bundle.putLong(VAR.KEY_USER_ID, id);
        bundle.putBoolean(VAR.KEY_IS_MY_PROFILE, isMyProfile);
        instance.setArguments(bundle);
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = getArguments().getLong(VAR.KEY_USER_ID);

        isMyProfile = getArguments().getBoolean(VAR.KEY_IS_MY_PROFILE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, layout);
        init();

        return layout;
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

        logoutButton = (Button) layout.findViewById(R.id.profile_logout);
        logoutButton.setOnClickListener(this);
        if (!isMyProfile) {
            logoutButton.setText(getString(R.string.follow));
            editProfile.setText("Message");
        }
//        getUser();
        getUser(userId);
        getUserPosts(userId);
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
/*

    private void getUser() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().getUserProfile(App.userId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject profile, Response response) {
                Retrofit.res(profile + "", response);

                MainActivity.progressBar.setVisibility(View.GONE);
//                updateUI(profile);
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
*/


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

    private void updateUI(Profile profil) {
        if (profil != null) {
            profile = profil;
            followersTv.setText(String.valueOf(profile.getFollowing()));
            followingTv.setText(String.valueOf(profile.getFollower()));
            nameTv.setText(profile.getUsername());
            Image.obj(getActivity()).setImage(profileImage, profile.getImage()
                    , R.drawable.galleryselected);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
                if (isMyProfile) {

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.logout)).setMessage(getString(R.string.logout_now))
                            .setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    getActivity().getSharedPreferences(VAR.PREF_NAME, 0).edit().clear().commit();
                                    dialog.dismiss();
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    getActivity().finish();

                                }
                            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    alertDialog.show();

                } else {
                    //unfollow
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
                    System.err.println("lat" + profile.getLatitude());
                    System.err.println("long" + profile.getLongitude());
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
}
