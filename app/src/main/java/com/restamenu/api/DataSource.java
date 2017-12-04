package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.model.content.Restaurant;

import java.util.List;

/**
 * @author Roodie
 */

public interface DataSource {

    void getRestaurants(@NonNull Integer cityId, @NonNull LoadRestaurantsCallback callback);

    //void getRestaurants(@NonNull Integer cityId, String keyword, Integer cusineId, Integer instituteId, Integer page,
    //                     boolean details, @NonNull LoadRestaurantsCallback callback);

    void getRestaurant(@NonNull String id, @NonNull GetRestaurantCallback callback);

    interface LoadRestaurantsCallback {

        void onNext(List<Restaurant> data);

        void onError(Throwable error);
    }

    interface GetRestaurantCallback {

        void onNext(Restaurant data);

        void onError(Throwable error);
    }


}
