package com.timore.vendor.views.home;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timore.vendor.R;
import com.timore.vendor.adapters.NotifRecyclerAdapter;
import com.timore.vendor.beanBojo.Notif;
import com.timore.vendor.control.App;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.views.MainActivity;
import com.timore.vendor.views.RecycleItemDecoration;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class NotificationFragment extends android.support.v4.app.Fragment {
    View layout;

    /*Views*/
    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_notification, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.notif_swipe_layout);
        recyclerView = (RecyclerView) layout.findViewById(R.id.notif_recyclerView);
        init();

        return layout;
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleItemDecoration());
        getNotification();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotification();
            }
        });
    }

    private void getNotification() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().getNotification(App.userId, new Callback<List<Notif>>() {
            @Override
            public void success(List<Notif> notif, Response response) {
                Retrofit.res(notif + "", response);
                MainActivity.progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.setAdapter(new NotifRecyclerAdapter(getActivity(), notif));
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
