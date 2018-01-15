package com.restamenu.api.service;

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
import com.restamenu.model.responce.ListResponce;
import com.restamenu.model.responce.Responce;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Documentation: https://docs.google.com/document/d/10nqhgx7Fzncotp52z9UHIDRfzTW8F-PYJ_EqdPsRBSo/edit
 */

public interface RestaurantService {

    /**
     * @param cityId      required
     * @param keyword     not required
     * @param cuisineIds    not required
     * @param instituteIds not required
     * @param page        not required
     * @param details     not required
     * @return
     */
    @NonNull
    @GET("getRestaurants")
    Flowable<ListResponce<Restaurant>> getRestaurants(@NonNull @Query("city_id") Integer cityId,
                                                      @Query("keyword") String keyword,
                                                      @Query("cuisine_id") String cuisineIds,
                                                      @Query("institute_id") String instituteIds,
                                                      @Query("page") Integer page,
                                                      @Query("details") boolean details);

    /**
     *
     * @param cityId required
     * @param geo not required
     * @param details not required
     * @return
     */
    @NonNull
    @GET("getNearRestaurants")
    Flowable<ListResponce<Restaurant>> getNearRestaurants(@NonNull @Query("city_id") Integer cityId,
                                                          @NonNull @Query("geo") String geo,
                                                          @NonNull @Query("width") Integer width,
                                                          @Query("details") boolean details);

    @NonNull
    @GET("getInstitutions")
    Flowable<ListResponce<Institute>> getInstitutions();

    @NonNull
    @GET("getCuisines")
    Flowable<ListResponce<Cusine>> getCuisines();

    /**
     * Возвращает данные по одному конкретному ресторану.
     *
     * @param restaurantId required
     * @param languageId   not required
     * @return
     */
    @NonNull
    @GET("getRestaurant")
    Flowable<Responce<Restaurant>> getRestaurant(@NonNull @Query("restaurant_id") Integer restaurantId,
                                                 @NonNull @Query("width") Integer width,
                                                 @Query("language_id") Integer languageId);

    @NonNull
    @GET("getCategories")
    Flowable<ListResponce<Category>> getCategories(@NonNull @Query("restaurant_id") Integer restaurantId,
                                                   @Query("service_id") Integer serviceId,
                                                   @Query("language_id") Integer languageId);

    @NonNull
    @GET("getCategory")
    Flowable<ListResponce<Product>> getCategory(@NonNull @Query("restaurant_id") Integer restaurantId,
                                                @Query("service_id") Integer serviceId,
                                                @NonNull @Query("category_id") Integer categoryId,
                                                @Query("language_id") Integer languageId,
                                                @Query("currency_id") Integer currencyId);

    @NonNull
    @GET("getGallery")
    Flowable<ListResponce<Image>> getGallery(@NonNull @Query("restaurant_id") Integer restaurantId);

    @NonNull
    @GET("getPromotions")
    Flowable<ListResponce<Promotion>> getPromotions(@NonNull @Query("restaurant_id") Integer restaurantId);

    @NonNull
    @GET("getLanguages")
    Flowable<ListResponce<Language>> getLanguages();

    @NonNull
    @GET("getCurrencies")
    Flowable<ListResponce<Currency>> getCurrencies(@Query("language_id") Integer languageId);


}
