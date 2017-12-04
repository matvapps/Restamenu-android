package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.BuildConfig;
import com.restamenu.RestamenuApp;
import com.restamenu.api.service.RestaurantService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.restamenu.BuildConfig.BASE_URL;

/**
 * @author Roodie
 */

public final class ApiFactory {

    public static final int cacheSize = 10 * 1024 * 1024; // 10 MiB

    private static OkHttpClient sClient;
    private static Retrofit sRetrofit;
    private static RestaurantService restaMenuService;

    @NonNull
    public static RestaurantService getService() {
        RestaurantService service = restaMenuService;
        if (service == null) {
            synchronized (ApiFactory.class) {
                service = restaMenuService;
                if (service == null) {
                    sRetrofit = buildRetrofit();
                    service = restaMenuService = sRetrofit.create(RestaurantService.class);
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
                .baseUrl(BASE_URL)
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
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        Cache cache = new Cache(RestamenuApp.get().getCacheDir(), cacheSize);
        return new OkHttpClient.Builder()
                .addInterceptor(keyInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }

    private static Interceptor keyInterceptor() {
        Interceptor intrceptor = null;

        intrceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        //.header("Content-Type", "application/json")
                        //.header("Accept", "application/json")
                        .header("key", BuildConfig.AUTH_KEY)
                        .header("Cache-Control", "max-age=30") // read from cache for 1/2 minute
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        };
        return intrceptor;
    }


}
