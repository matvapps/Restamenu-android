package com.restamenu.data.database;

import android.support.annotation.NonNull;

import com.restamenu.api.DataSource.GetRestaurantCallback;
import com.restamenu.api.DataSource.LoadRestaurantsCallback;
import com.restamenu.model.content.Restaurant;

import java.util.List;

/**
 * Concrete implementation of a data source as a db.
 */
public class LocalRepository {


    public void getRestaurants(@NonNull Integer cityId, @NonNull LoadRestaurantsCallback callback) {
    }

    public void setRestaurants(List<Restaurant> data) {

    }

    public void setNearRestaurants(List<Restaurant> data) {

    }

    public void getRestaurant(@NonNull String id, @NonNull GetRestaurantCallback callback) {

    }

    public void logout() {

    }
}
