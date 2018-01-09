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

    private int width;

    public MainPresenter(int width)
    {
        this.width = width;
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

    public void performSearch(String keyword) {
        RepositoryProvider.getAppRepository()
                .getRestaurants(1, keyword, null, null, 1, true)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(restaurants -> {
                    view.setFoundedRestaurants(restaurants);
                    view.showLoading(false);
                }, throwable -> view.showError());
    }

    private void loadNearRestaurants(int width) {
        view.showLoading(true);

        RepositoryProvider.getAppRepository().getNearRestaurants(1, "25.1948827,55.2738285", width)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(restaurants -> {
                    view.setNearRestaurants(restaurants);
                    view.showLoading(false);
                }, throwable -> view.showError());
    }

    private void loadRestaurants() {
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
    }

    public void loadSuggestions(String keyword, int cuisineId, int instituteId) {

        RepositoryProvider.getAppRepository().getRestaurants(1, keyword, null, null, 1, true)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(restaurants -> {
                    view.setSuggestion(restaurants);
                }, throwable -> view.showError());

    }

    private void loadInstitutions() {
        view.showLoading(true);

        RepositoryProvider.getAppRepository().getInstitutions()
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(institutes -> {
                    view.setInstitutions(institutes);
                    view.showLoading(false);
                    loadRestaurants();
                    loadNearRestaurants(width);
                }, throwable -> view.showError());
    }

    private void loadCuisines() {
        view.showLoading(true);

        RepositoryProvider.getAppRepository().getCusines()
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(cusines -> {
                    view.setCusines(cusines);
                    view.showLoading(false);
                }, throwable -> view.showError());
    }

    private void loadData() {
        loadInstitutions();
        loadCuisines();
    }
}
