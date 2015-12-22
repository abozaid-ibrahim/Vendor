package com.timore.vendor.views.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timore.vendor.R;
import com.timore.vendor.adapters.PostsRecyclerAdapter;
import com.timore.vendor.beanBojo.Post;
import com.timore.vendor.control.App;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.views.MainActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainFragment extends android.support.v4.app.Fragment {
    View layout;

    /*Views*/
    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_main, container, false);
        swipeRefreshLayout= (SwipeRefreshLayout) layout.findViewById(R.id.main_swipe_layout);
        recyclerView= (RecyclerView) layout.findViewById(R.id.main_recyclerView);
        init();

        return layout;
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 10;
            }
        });
        getPosts();
//        swipeRefreshLayout.setColorSchemeColors();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getPosts();
            }
        });
    }

    private void getPosts() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().getPosts(App.userId, new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                Retrofit.res(posts + "", response);
                MainActivity.progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.setAdapter(new PostsRecyclerAdapter(getActivity(), posts));
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


}
