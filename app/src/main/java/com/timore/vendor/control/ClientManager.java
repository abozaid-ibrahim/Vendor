package com.timore.vendor.control;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Abuzeid on 2/6/2016.
 */
public class ClientManager {


    private static LocalBroadcastManager broadcaster;
    private static ClientManager instance;

    private ClientManager() {
    }

    public static ClientManager getInstance(Context context) {
        if (instance == null) {
            broadcaster = LocalBroadcastManager.getInstance(context);
            instance = new ClientManager();
        }
        return instance;
    }

    public boolean connect(Context context) {
        return true;
    }


}
