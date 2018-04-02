package com.restamenu.category;

import android.support.annotation.NonNull;

import com.restamenu.api.RepositoryProvider;
import com.restamenu.base.Presenter;
import com.restamenu.rx.BaseSchedulerProvider;
import com.restamenu.rx.SchedulerProvider;
import com.restamenu.util.Logger;

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

    public void loadCurrencies(int language_id) {
        RepositoryProvider.getAppRepository().getCurrencies(language_id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(currencies -> {
                    view.setCurrencies(currencies);
                    loadCategories();
                }, throwable -> view.showError());
    }

    public void loadProducts(int restaurantId, int categoryId) {
        //refreshLayout.setRefreshing(true);
        view.showLoading(true);
        RepositoryProvider.getAppRepository().getCategoryProducts(restaurantId, categoryId)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(products -> {
                    Logger.log("Products: " + products.toString());
                    //refreshLayout.setRefreshing(false);
                    view.showLoading(false);
                    view.setProducts(products);
                }, throwable -> view.showError());
    }


    public void loadCategories() {
        RepositoryProvider.getAppRepository().getCategories(restaurantId, serviceId)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(categories -> {
                    view.setData(categories);
                    view.showLoading(false);
                }, throwable -> view.showError());
    }


    public void loadData(int languageId) {
        view.showLoading(true);
        RepositoryProvider.getAppRepository().getLanguages()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(languages -> {
                    view.setLanguages(languages);
                    loadCurrencies(languageId);
                }, throwable -> view.showError());

    }

}
