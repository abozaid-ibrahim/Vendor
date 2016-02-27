package com.timore.vendor.home;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.timore.vendor.R;
import com.timore.vendor.adapters.UsersRecyclerAdapter;
import com.timore.vendor.beanBojo.User;
import com.timore.vendor.control.App;
import com.timore.vendor.control.Retrofit;
import com.timore.vendor.views.MainActivity;
import com.timore.vendor.views.RecycleItemDecoration;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SearchFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static SearchFragment instance;
    View layout;
    /*Views*/
    RecyclerView recyclerView;
    Button submitSeach;
    EditText searchEt;

    public static SearchFragment getInstance() {
        if (instance == null) {
            instance = new SearchFragment();
        }
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.search_recyclerView);
        submitSeach = (Button) layout.findViewById(R.id.search_button_search);
        searchEt = (EditText) layout.findViewById(R.id.search_et_search);
        init();
        submitSeach.setOnClickListener(this);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                    search(v);
                return false;
            }
        });
        return layout;
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleItemDecoration());
        getUsers();
    }

    private void getUsers() {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().getAllUsers(App.userId, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                Retrofit.res(users + "", response);
                MainActivity.progressBar.setVisibility(View.GONE);
                if (users != null) {
                    User me = null;
                    for (User usr : users) {
                        if (usr.getId() == App.userId) {
                            me = usr;
                            break;
                        }
                    }
                    if (me != null)
                        users.remove(me);
                    recyclerView.setAdapter(new UsersRecyclerAdapter(getActivity(), users));
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

    private void search(String username) {
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        Retrofit.getInstance().searchInUsers(App.userId, username, "", "", "", new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                Retrofit.res(users + "", response);
                MainActivity.progressBar.setVisibility(View.GONE);
                if (users != null)
                    recyclerView.setAdapter(new UsersRecyclerAdapter(getActivity(), users));
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

    private void search(View v) {
        if (searchEt.getText().toString().isEmpty())
            Snackbar.make(v, "write what you searching for", Snackbar.LENGTH_LONG).show();
        else {
            App.hideSoftInput(getActivity());
            search(searchEt.getText().toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button_search:
                search(v);
                break;
        }
    }
}
