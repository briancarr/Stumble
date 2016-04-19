package com.stumbleapp.Location;

import android.app.IntentService;
import android.content.Intent;


public class AndroidLocationManager extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AndroidLocationManager(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}