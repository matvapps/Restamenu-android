package com.restamenu.api.service;

import android.support.annotation.NonNull;

import com.restamenu.model.content.Category;
import com.restamenu.model.content.Cuisine;
import com.restamenu.model.content.Restaurant;
import com.restamenu.model.responce.CategoriesResponce;
import com.restamenu.model.responce.InstitutionsResponse;
import com.restamenu.model.responce.RestaurantsResponce;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Documentation: https://docs.google.com/document/d/10nqhgx7Fzncotp52z9UHIDRfzTW8F-PYJ_EqdPsRBSo/edit
 */

public interface RestaurantService {

    /**
     * @param cityId      required
     * @param keyword     not required
     * @param cusineId    not required
     * @param instituteId not required
     * @param page        not required
     * @param details     not required
     * @return
     */
    @NonNull
    @GET("getRestaurants")
    Call<RestaurantsResponce> getRestaurants(@NonNull @Query("city_id") Integer cityId,
                                             @Query("keyword") String keyword,
                                             @Query("cusine_id") Integer cusineId,
                                             @Query("institute_id") Integer instituteId,
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
    Call<RestaurantsResponce> getNearRestaurants(@NonNull @Query("city_id") Integer cityId,
                                              @Query("geo") String geo,
                                              @Query("details") boolean details);

    @NonNull
    @GET("getInstitutions")
    Call<InstitutionsResponse> getInstitutions();

    @NonNull
    @GET("getCusines")
    Call<List<Cuisine>> getCuisines();

    /**
     * Возвращает данные по одному конкретному ресторану.
     *
     * @param restaurantId required
     * @param languageId   not required
     * @return
     */
    @NonNull
    @GET("getRestaurant")
    Call<Restaurant> getRestaurant(@NonNull @Query("restaurant_id") Integer restaurantId,
                                   @Query("language_id") Integer languageId);

    @NonNull
    @GET("getCategories")
    Call<CategoriesResponce> getCategories(@NonNull @Query("restaurant_id") Integer restaurantId,
                                           @NonNull @Query("service_id") Integer serviceId,
                                           @Query("language_id") Integer languageId);

    @NonNull
    @GET("getCategory")
    Call<Category> getCategory(@NonNull @Query("restaurant_id") Integer restaurantId,
                               @NonNull @Query("service_id") Integer serviceId,
                               @NonNull @Query("category_id") Integer categoryId,
                               @Query("language_id") Integer languageId,
                               @Query("currency_id") Integer currencyId);



}
