package com.restamenu.restaurant;

import android.support.annotation.NonNull;

import com.restamenu.api.RepositoryProvider;
import com.restamenu.base.Presenter;
import com.restamenu.rx.BaseSchedulerProvider;
import com.restamenu.rx.SchedulerProvider;
import com.restamenu.util.ListUtils;
import com.restamenu.util.Logger;

import io.reactivex.Flowable;

/**
 * @author Roodie
 */

public class RestaurantsPresenter implements Presenter<RestaurantView> {

    private final BaseSchedulerProvider schedulerProvider;
    private RestaurantView view;
    private Integer restaurantId;

    public RestaurantsPresenter(@NonNull Integer restaurantId) {
        this.restaurantId = restaurantId;
        schedulerProvider = SchedulerProvider.getInstance();
    }

    @Override
    public void attachView(RestaurantView view) {
        this.view = view;

    }

    @Override
    public void detachView() {
        this.view = null;

    }

    public void loadData() {
        view.showLoading(true);
        RepositoryProvider.getAppRepository().getRestaurant(restaurantId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(restaurant -> {
                    view.setData(restaurant);
                    view.showLoading(false);
                    if (!ListUtils.isEmpty(restaurant.getServices()))
                        loadCategories(restaurantId, restaurant.getServices().get(0));
                    loadGallery(restaurantId);
                    loadPromotions(restaurantId);
                }, throwable -> view.showError());
    }


    private void loadCategories(int restaurantId, int serviceId) {
        RepositoryProvider.getAppRepository().getCategories(restaurantId, serviceId)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(categories -> {
                    view.setCategories(categories);
                    if (!ListUtils.isEmpty(categories))
                        loadProducts(restaurantId, serviceId, categories.get(0).geId());
                }, throwable -> view.showError());
    }

    private void loadGallery(int restaurantId) {
        RepositoryProvider.getAppRepository().getGallery(restaurantId)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(images -> {
                    view.setGallery(images);
                }, throwable -> view.showError());
    }

    private void loadPromotions(int restaurantId) {
        RepositoryProvider.getAppRepository().getPromotions(restaurantId)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(promotions -> {
                    view.setPromotions(promotions);
                }, throwable -> view.showError());
    }

    private void loadProducts(int restaurantId, int serviceId, int categoryId) {
        RepositoryProvider.getAppRepository().getCategoryProducts(restaurantId, serviceId, categoryId)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(products -> {
                    Logger.log("Products: " + products.toString());
                }, throwable -> view.showError());
    }
}
