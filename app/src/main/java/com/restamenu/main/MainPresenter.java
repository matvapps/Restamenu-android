package com.restamenu.main;

import com.restamenu.api.RepositoryProvider;
import com.restamenu.base.Presenter;
import com.restamenu.rx.BaseSchedulerProvider;
import com.restamenu.rx.SchedulerProvider;

import io.reactivex.Flowable;

/**
 * @author Roodie
 */

public class MainPresenter implements Presenter<MainView> {

    private final BaseSchedulerProvider schedulerProvider;
    private MainView view;

    public MainPresenter() {
        schedulerProvider = SchedulerProvider.getInstance();
    }

    @Override
    public void attachView(MainView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void init() {
        loadData();
    }

    public void loadData() {
        view.showLoading(true);

        RepositoryProvider.getAppRepository().getRestaurants(1, 1)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(restaurants -> {
                    view.setData(restaurants);
                    view.showLoading(false);
                }, throwable -> view.showError());

        RepositoryProvider.getAppRepository().getNearRestaurants(1, "56.8457373,60.5972259")
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(restaurants -> {
                    view.setNearRestaurants(restaurants);
                    view.showLoading(false);
                }, throwable -> view.showError());

        RepositoryProvider.getAppRepository().getInstitutions()
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(institutes -> {
                    view.setInstitutions(institutes);
                    view.showLoading(false);
                }, throwable -> view.showError());


    }
}
