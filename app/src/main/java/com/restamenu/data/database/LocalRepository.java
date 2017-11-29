package com.restamenu.data.database;

import android.support.annotation.NonNull;

import com.restamenu.api.DataSource;

/**
 * Concrete implementation of a data source as a db.
 */
public class LocalRepository implements DataSource {


    @Override
    public void getRestaurants(@NonNull LoadRestaurantsCallback callback) {

    }

    @Override
    public void getRestaurant(@NonNull String id, @NonNull GetRestaurantCallback callback) {

    }

    public void logout() {

    }
}
