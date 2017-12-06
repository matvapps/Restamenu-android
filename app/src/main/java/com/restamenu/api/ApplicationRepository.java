package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.data.database.LocalRepository;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Institute;
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
    public void getInstitutions(@NonNull final GetInstitutionsCallback callback) {
        remoteRepository.getInstitutions(new GetInstitutionsCallback() {
            @Override
            public void onNext(List<Institute> data) {
                callback.onNext(data);
            }

            @Override
            public void onError(Throwable error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void getNearRestaurants(@NonNull Integer cityId, @NonNull String geo, final @NonNull LoadRestaurantsCallback callback) {
        remoteRepository.getNearRestaurants(cityId, geo, true, new LoadRestaurantsCallback() {
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
    public void getRestaurant(@NonNull Integer id, @NonNull final GetRestaurantCallback callback) {
        remoteRepository.getRestaurant(id, new GetRestaurantCallback() {
            @Override
            public void onNext(Restaurant data) {
                callback.onNext(data);
                //TODO update in db
            }

            @Override
            public void onError(Throwable error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void getCategories(@NonNull Integer restaurantId, @NonNull Integer serviceId, @NonNull final GetCategoriesCallback callback) {
        remoteRepository.getCategories(restaurantId, serviceId, new GetCategoriesCallback() {
            @Override
            public void onNext(List<Category> data) {
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
    public void getCategory(@NonNull Integer restaurantId, @NonNull Integer serviceId, @NonNull Integer categoryId, @NonNull final GetCategoryCallback callback) {
        remoteRepository.getCategory(restaurantId, serviceId, categoryId, new GetCategoryCallback() {
            @Override
            public void onNext(Category data) {
                callback.onNext(data);
                //TODO update in db
            }

            @Override
            public void onError(Throwable error) {
                callback.onError(error);
            }
        });
    }
}
