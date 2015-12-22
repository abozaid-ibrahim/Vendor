package com.timore.vendor.control;

import com.google.gson.JsonObject;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by usear on 12/16/2015.
 */
public class RequestManager {
    public static void uploadImage(File file) {
        Retrofit.getInstance().uploadImage(file, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject object, Response response) {
                Retrofit.res(object + "", response);

            }

            @Override
            public void failure(RetrofitError error) {
                Retrofit.failure(error);

            }
        });
    }
}
