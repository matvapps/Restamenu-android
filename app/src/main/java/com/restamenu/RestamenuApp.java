package com.restamenu;

import android.app.Application;
import android.support.annotation.NonNull;

import com.restamenu.data.RestaMenuPreferences;
import com.restamenu.state.ApplicationState;
import com.restamenu.state.InMemoryCache;

/**
 * Created by Alexandr.
 */

public class RestamenuApp extends Application {

    private static RestamenuApp sInstance;

    @NonNull
    public static RestamenuApp get() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        initApi();

//        final Fabric fabric = new Fabric.Builder(this)
//                .kits(new Crashlytics(), new Answers())
//                .debuggable(BuildConfig.DEBUG ? true : false)
//                .build();
//        Fabric.with(fabric);
//        SQLite.initialize(this);

    }

    private void initApi() {
        ApplicationState.setPreferences(new RestaMenuPreferences(this));
        ApplicationState.setMemoryCache(new InMemoryCache());

    }
}
