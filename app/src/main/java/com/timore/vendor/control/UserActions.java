package com.timore.vendor.control;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.timore.vendor.R;
import com.timore.vendor.adapters.PostsRecyclerAdapter;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by usear on 11/29/2015.
 */
public class UserActions {
    private static Context context;

    private UserActions() {

    }

    private static UserActions userActions;

    public static UserActions getInstance(Context context) {
        UserActions.context = context;
        if (userActions == null)
            userActions = new UserActions();
        return userActions;
    }

    public void followUser(long followerId) {
        Retrofit.getInstance().followUser(1, App.userId, followerId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);

            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(context))
                    Toast.makeText(context, context.getString(R.string.no_net), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void unFollowUser(long followerId) {
        Retrofit.getInstance().unFollowUser(App.userId, followerId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);

            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(context))
                    Toast.makeText(context, context.getString(R.string.no_net), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void like(final PostsRecyclerAdapter adapter, final int position, long postId) {
        Retrofit.getInstance().likePost(1, App.userId, postId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject users, Response response) {
                Retrofit.res(users + "", response);
                if (adapter != null)
                    adapter.notifyItemChanged(position);

            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(context))
                    Toast.makeText(context, context.getString(R.string.no_net), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void unLike(final PostsRecyclerAdapter adapter, final int position, long postId) {
        Retrofit.getInstance().unLikePost(1, App.userId, postId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject users, Response response) {
                Retrofit.res(users + "", response);
                if (adapter != null)
                    adapter.notifyItemChanged(position);

            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);
                if (!App.isConnected(context))
                    Toast.makeText(context, context.getString(R.string.no_net), Toast.LENGTH_LONG).show();

            }
        });
    }

}
