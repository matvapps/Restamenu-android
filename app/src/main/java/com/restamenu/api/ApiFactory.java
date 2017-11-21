package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.BuildConfig;
import com.restamenu.RestamenuApp;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Roodie
 */

public final class ApiFactory {

    public static final int cacheSize = 10 * 1024 * 1024; // 10 MiB

    private static OkHttpClient sClient;
    private static Retrofit sRetrofit;
    private static RestaMenuService restaMenuService;

    @NonNull
    public static RestaMenuService getService() {
        RestaMenuService service = restaMenuService;
        if (service == null) {
            synchronized (ApiFactory.class) {
                service = restaMenuService;
                if (service == null) {
                    sRetrofit = buildRetrofit();
                    service = restaMenuService = sRetrofit.create(RestaMenuService.class);
                }
            }
        }
        return service;
    }

    public static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            sRetrofit = buildRetrofit();
        }
        return sRetrofit;
    }

    @NonNull
    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("base_url")
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @NonNull
    private static OkHttpClient getClient() {
        OkHttpClient client = sClient;
        if (client == null) {
            synchronized (ApiFactory.class) {
                client = sClient;
                if (client == null) {
                    client = sClient = buildClient();
                }
            }
        }
        return client;
    }

    @NonNull
    private static OkHttpClient buildClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        /**
         * Can be disabled/enabled in DEBUG_MODE.
         */
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        Cache cache = new Cache(RestamenuApp.get().getCacheDir(), cacheSize);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }


}
