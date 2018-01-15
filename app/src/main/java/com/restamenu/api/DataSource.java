package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.model.content.Category;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Language;
import com.restamenu.model.content.Product;
import com.restamenu.model.content.Promotion;
import com.restamenu.model.content.Restaurant;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author Roodie
 */

public interface DataSource {

    Flowable<List<Restaurant>> getRestaurants(@NonNull Integer cityId, int page);

    Flowable<List<Restaurant>> getRestaurants(@NonNull Integer cityId, String keyword, String cuisineIds, String instituteIds, Integer page, boolean details);

    Flowable<List<Category>> getCategories(@NonNull Integer restaurantId, @NonNull Integer serviceId);

    Flowable<List<Promotion>> getPromotions(@NonNull Integer restaurantId);

    Flowable<Restaurant> getRestaurant(@NonNull Integer id, @NonNull Integer width);

    Flowable<List<Language>> getLanguages();

    Flowable<List<Currency>> getCurrencies(Integer languageId);

    Flowable<List<Restaurant>> getNearRestaurants(@NonNull Integer cityId, @NonNull String geo, @NonNull Integer width);

    Flowable<List<Institute>> getInstitutions();

    Flowable<List<Cusine>> getCusines();

    void refreshCusines();

    void refreshInstitutions();

    Flowable<List<Image>> getGallery(@NonNull Integer restaurantId);

    Flowable<List<Product>> getCategoryProducts(@NonNull Integer restaurantId, @NonNull Integer categoryId);



}
