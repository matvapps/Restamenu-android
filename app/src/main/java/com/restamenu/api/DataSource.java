package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.model.content.Category;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;

import java.util.List;

/**
 * @author Roodie
 */

public interface DataSource {

    void getRestaurants(@NonNull Integer cityId, @NonNull LoadRestaurantsCallback callback);

    void getCategories(@NonNull Integer restaurantId, @NonNull Integer serviceId, @NonNull GetCategoriesCallback callback);

    void getCategory(@NonNull Integer restaurantId, @NonNull Integer serviceId, @NonNull Integer categoryId, @NonNull GetCategoryCallback callback);

    void getRestaurant(@NonNull Integer id, @NonNull GetRestaurantCallback callback);

    void getNearRestaurants(@NonNull Integer cityId, @NonNull String geo, @NonNull LoadRestaurantsCallback callback);

    void getInstitutions(@NonNull GetInstitutionsCallback callback);

    interface LoadRestaurantsCallback {
        void onNext(List<Restaurant> data);
        void onError(Throwable error);
    }

    interface GetRestaurantCallback {
        void onNext(Restaurant data);

        void onError(Throwable error);
    }

    interface GetCategoryCallback {
        void onNext(Category data);

        void onError(Throwable error);
    }

    interface GetCategoriesCallback {
        void onNext(List<Category> data);

        void onError(Throwable error);
    }

    interface GetInstitutionsCallback {
        void onNext(List<Institute> data);

        void onError(Throwable error);
    }


}
