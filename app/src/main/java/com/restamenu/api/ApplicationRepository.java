package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.data.database.LocalRepository;
import com.restamenu.model.content.Restaurant;

import java.util.List;

/**
 * @author Roodie
 */

public class ApplicationRepository implements DataSource {

    private final LocalRepository localRepository;
    private final RemoteRepository remoteRepository;

    public ApplicationRepository(@NonNull LocalRepository localRepository, @NonNull RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    @Override
    public void getRestaurants(@NonNull Integer cityId, @NonNull final LoadRestaurantsCallback callback) {
        //Try to fetch from local db. If empty - from remote server.
        remoteRepository.getRestaurants(cityId, new LoadRestaurantsCallback() {
            @Override
            public void onNext(List<Restaurant> data) {
                callback.onNext(data);
                //TODO save in db
            }

            @Override
            public void onError(Throwable error) {
                callback.onError(error);
            }
        });
    }


    @Override
    public void getRestaurant(@NonNull String id, @NonNull GetRestaurantCallback callback) {

    }
}
