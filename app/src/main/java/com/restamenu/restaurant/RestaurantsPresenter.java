package com.restamenu.restaurant;

import android.support.annotation.NonNull;

import com.restamenu.api.RepositoryProvider;
import com.restamenu.base.Presenter;
import com.restamenu.rx.BaseSchedulerProvider;
import com.restamenu.rx.SchedulerProvider;
import com.restamenu.util.ListUtils;

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
}
