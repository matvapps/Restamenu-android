package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.api.DataSource.GetRestaurantCallback;
import com.restamenu.api.DataSource.LoadRestaurantsCallback;
import com.restamenu.api.service.RestaurantService;
import com.restamenu.model.responce.RestaurantsResponce;
import com.restamenu.util.AppExecutors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author Roodie
 */

public class RemoteRepository {

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;
    private final RestaurantService restaMenuService;
    private final AppExecutors executors;

    public RemoteRepository(AppExecutors executors, RestaurantService restaMenuService) {
        this.executors = executors;
        this.restaMenuService = restaMenuService;
    }

    public void getRestaurants(@NonNull final Integer cityId, final @NonNull LoadRestaurantsCallback callback) {
        getRestaurants(cityId, null, null, null, null, true, callback);
    }

    public void getRestaurants(@NonNull final Integer cityId, final String keyword, final Integer cusineId, final Integer instituteId, final Integer page, final boolean details, @NonNull final LoadRestaurantsCallback callback) {
        executors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<RestaurantsResponce> call = restaMenuService.getRestaurants(cityId, keyword, cusineId, instituteId, page, details);
                call.enqueue(new Callback<RestaurantsResponce>() {
                    @Override
                    public void onResponse(Call<RestaurantsResponce> call, Response<RestaurantsResponce> response) {
                        if (response.code() == 200) {
                            callback.onNext(response.body().getRestaurants());
                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantsResponce> call, Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        });
    }

    public void getRestaurant(final @NonNull String id, final @NonNull GetRestaurantCallback callback) {

    }
}
