package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.api.service.RestaurantService;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;
import com.restamenu.model.responce.ListResponce;
import com.restamenu.model.responce.Responce;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


/**
 * @author Roodie
 */

public class RemoteRepository {

    private final RestaurantService restaMenuService;

    public RemoteRepository(RestaurantService restaMenuService) {
        this.restaMenuService = restaMenuService;
    }

    public Flowable<List<Restaurant>> getRestaurants(int cityId, int page) {
        return restaMenuService.getRestaurants(cityId, null, null, null, page, true)
                .map(ListResponce<Restaurant>::getData);
    }

    public Flowable<List<Restaurant>> getRestaurants(@NonNull Integer cityId, String keyword, Integer cusineId, Integer instituteId, Integer page, boolean details) {
        return restaMenuService.getRestaurants(cityId, keyword, cusineId, instituteId, page, details)
                .map(ListResponce<Restaurant>::getData);
    }

    public Flowable<List<Restaurant>> getNearRestaurants(@NonNull Integer cityId, String geo, boolean details) {
        return restaMenuService.getNearRestaurants(cityId, geo, details)
                .map(ListResponce<Restaurant>::getData);
    }

    public Flowable<Restaurant> getRestaurant(final @NonNull Integer id) {
        return restaMenuService.getRestaurant(id, null)
                .map(Responce<Restaurant>::getItem);
    }

    public Flowable<List<Category>> getCategories(@NonNull Integer restId, Integer serviceId) {
        return restaMenuService.getCategories(restId, serviceId, null)
                .map(ListResponce<Category>::getData);
    }

    public Single<Category> getCategory(@NonNull final Integer restId, final Integer serviceId, final Integer categoryId) {
        return restaMenuService.getCategory(restId, serviceId, categoryId, null, null);
    }

    public Flowable<List<Cusine>> getCusines() {
        return restaMenuService.getCuisines()
                .map(ListResponce<Cusine>::getData);
    }

    public Flowable<List<Institute>> getInstitutions() {
        return restaMenuService.getInstitutions()
                .map(ListResponce<Institute>::getData);
    }

    public Flowable<List<Image>> getGallery(@NonNull Integer restaurantId) {
        return restaMenuService.getGallery(restaurantId)
                .map(ListResponce<Image>::getData);
    }


}
