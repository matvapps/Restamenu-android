package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.model.content.Category;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * @author Roodie
 */

public interface DataSource {

    Flowable<List<Restaurant>> getRestaurants(@NonNull Integer cityId, int page);

    Flowable<List<Category>> getCategories(@NonNull Integer restaurantId, @NonNull Integer serviceId);

    Single<Category> getCategory(@NonNull Integer restaurantId, @NonNull Integer serviceId, @NonNull Integer categoryId);

    Flowable<Restaurant> getRestaurant(@NonNull Integer id);

    Flowable<List<Restaurant>> getNearRestaurants(@NonNull Integer cityId, @NonNull String geo);

    Flowable<List<Institute>> getInstitutions();

    Flowable<List<Cusine>> getCusines();

    void refreshCusines();

    void refreshInstitutions();

    Flowable<List<Image>> getGallery(@NonNull Integer restaurantId);



}
