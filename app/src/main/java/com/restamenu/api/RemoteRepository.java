package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.api.service.RestaurantService;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Product;
import com.restamenu.model.content.Promotion;
import com.restamenu.model.content.Restaurant;
import com.restamenu.model.responce.ListResponce;
import com.restamenu.model.responce.Responce;

import java.util.List;

import io.reactivex.Flowable;


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

    public Flowable<List<Restaurant>> getNearRestaurants(@NonNull Integer cityId, String geo, Integer width, boolean details) {
        return restaMenuService.getNearRestaurants(cityId, geo, width, details)
                .map(ListResponce<Restaurant>::getData);
    }

    public Flowable<Restaurant> getRestaurant(final @NonNull Integer id,final @NonNull Integer width, int language_id) {
        return restaMenuService.getRestaurant(id, width, language_id)
                .map(Responce<Restaurant>::getItem);
    }

    public Flowable<List<Category>> getCategories(@NonNull Integer restId, @NonNull Integer serviceId) {
        return restaMenuService.getCategories(restId, serviceId, null)
                .map(ListResponce<Category>::getData);
    }

    public Flowable<List<Product>> getCategoryProducts(@NonNull final Integer restId, final Integer categoryId) {
        return restaMenuService.getCategory(restId, null, categoryId, null, null)
                .map(ListResponce<Product>::getData);
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

    public Flowable<List<Promotion>> getPromotions(@NonNull Integer restaurantId) {
        return restaMenuService.getPromotions(restaurantId)
                .map(ListResponce<Promotion>::getData);
    }


}
