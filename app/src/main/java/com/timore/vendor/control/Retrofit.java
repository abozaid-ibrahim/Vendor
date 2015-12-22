package com.timore.vendor.control;

import java.util.Iterator;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;

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
        System.err.println("STATUS: " + response.getStatus());
        System.err.println("REASON: " + response.getReason());
        System.err.println("HEADER-------------@");
        Iterator it = response.getHeaders().iterator();
        while (it.hasNext()) {
            Header h = ((Header) it.next());
            System.err.println("NAME: " + h.getName() + "=" + h.getValue());
        }
        System.err.println("################## JSON #######################");
        System.err.println(res);

    }

    public static void failure(RetrofitError error) {
        System.err.println("**??????????????????????? RetrofitError ???????????????????????**");
        System.err.println("MESSAGE **" + error.getMessage());
        System.err.println("URL **" + error.getUrl());
        System.err.println("CAUSE **" + error.getCause());
        System.err.println("STACK TRACE **" + error.getStackTrace());

    }
}
