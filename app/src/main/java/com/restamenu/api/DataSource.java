package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.model.content.Restaurant;

import java.util.List;

/**
 * @author Roodie
 */

public interface DataSource {

    void getRestaurants(@NonNull LoadRestaurantsCallback callback);

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
