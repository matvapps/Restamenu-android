package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.api.service.RestaMenuService;
import com.restamenu.model.content.Restaurant;
import com.restamenu.util.AppExecutors;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Roodie
 */

public class RemoteRepository implements DataSource {

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;
    private final RestaMenuService restaMenuService;
    private final AppExecutors executors;

    public RemoteRepository(AppExecutors executors, RestaMenuService restaMenuService) {
        this.executors = executors;
        this.restaMenuService = restaMenuService;
    }

    @Override
    public void getRestaurants(final @NonNull LoadRestaurantsCallback callback) {
        executors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<List<Restaurant>> call = restaMenuService.getRestaurants();
                call.enqueue(new Callback<List<Restaurant>>() {
                    @Override
                    public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                        if (response.code() == 200) {
                            callback.onNext(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        });
    }

    @Override
    public void getRestaurant(final @NonNull String id, final @NonNull GetRestaurantCallback callback) {

    }
}
