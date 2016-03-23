package com.timore.vendor.control;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by ZEID on 7/23/2015.
 */
public class Retrofit {

    private static ServiceInterface searchService;

    private Retrofit() {


    }

    public static synchronized ServiceInterface getInstance() {
        if (searchService == null) {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(VAR.SERVER_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            //
            searchService = restAdapter.create(ServiceInterface.class);
        }
        return searchService;
    }

    /*TEST*/
    public static void res(String res, retrofit.client.Response response) {
        System.err.println("################## REQUEST #######################");
        System.err.println("URL: " + response.getUrl());
        System.err.println(res);

    }

    public static void failure(RetrofitError error) {
        /*System.err.println("**??????????????????????? RetrofitError ???????????????????????**");
        System.err.println("MESSAGE **" + error.getMessage());
        System.err.println("URL **" + error.getUrl());
        System.err.println("CAUSE **" + error.getCause());*/


    }
}
