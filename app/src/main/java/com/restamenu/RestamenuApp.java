package com.restamenu;

import android.app.Application;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.restamenu.api.ApiFactory;
import com.restamenu.api.ApplicationRepository;
import com.restamenu.api.RemoteRepository;
import com.restamenu.api.RepositoryProvider;
import com.restamenu.data.database.LocalRepository;
import com.restamenu.data.preferences.KeyValueStorage;

import io.fabric.sdk.android.Fabric;

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

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(BuildConfig.DEBUG ? true : false)
                .build();
        Fabric.with(fabric);

        initApi();
//        SQLite.initialize(this);

    }

    private void initApi() {
        RepositoryProvider.setPreferences(new KeyValueStorage(this));
        RepositoryProvider.setAppRepository(new ApplicationRepository(new LocalRepository(),
                new RemoteRepository(ApiFactory.getService())));
    }
}
