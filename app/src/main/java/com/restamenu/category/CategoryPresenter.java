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

    public void loadCurrencies(int language_id) {
        view.showLoading(true);
        RepositoryProvider.getAppRepository().getCurrencies(language_id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(currencies -> {
                    view.setCurrencies(currencies);
                    loadProducts();
                    view.showLoading(false);
                }, throwable -> view.showError());
    }


    public void loadProducts() {
        view.showLoading(true);
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
        RepositoryProvider.getAppRepository().getLanguages()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(languages -> {
                    view.setLanguages(languages);
                    loadCurrencies(languageId);
                }, throwable -> view.showError());

    }

}
