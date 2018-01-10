package com.restamenu.category;

import android.support.annotation.NonNull;

import com.restamenu.api.RepositoryProvider;
import com.restamenu.base.Presenter;
import com.restamenu.rx.BaseSchedulerProvider;
import com.restamenu.rx.SchedulerProvider;

import io.reactivex.Flowable;

/**
 * Created by Alexandr.
 */

public class CategoryPresenter implements Presenter<CategoryView> {

    private CategoryView view;
    private Integer restaurantId;
    private Integer serviceId;
    private BaseSchedulerProvider schedulerProvider;


    public CategoryPresenter(@NonNull Integer restaurantId, @NonNull Integer serviceId) {
        this.restaurantId = restaurantId;
        this.serviceId = serviceId;
        schedulerProvider = SchedulerProvider.getInstance();
    }

    @Override
    public void attachView(CategoryView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;

    }


    public void loadData() {
        RepositoryProvider.getAppRepository().getCategories(restaurantId, serviceId)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(categories -> {
                    view.setData(categories);
                }, throwable -> view.showError());
    }



    /*public void loadProducts(int restaurantId, int categoryId) {
        RepositoryProvider.getAppRepository().getCategoryProducts(restaurantId, categoryId)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(products -> {
                    view.setProducts(products);
                });
    }*/


}
