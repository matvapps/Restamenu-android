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

    public MainPresenter(int width) {
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

    public void init(int page) {
        loadData(page, null, null, null);
    }



    private void loadNearRestaurants(int width) {
        view.showLoading(true);

        RepositoryProvider.getAppRepository().getNearRestaurants(1, "25.1948827,55.2738285", width)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(restaurants -> {
                    view.showLoading(false);
                    view.setNearRestaurants(restaurants);
                }, throwable -> view.showError());
    }

//    public void performSearch(int page, String keyword, String filterCuisines, String filterInstitutes){
//        view.showLoading(true);
//        RepositoryProvider.getAppRepository().getRestaurants(1, keyword, filterCuisines, filterInstitutes, page, true)
//                .flatMap(Flowable::fromIterable)
//                .toList()
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.ui())
//                .subscribe(restaurants -> {
//                    view.showLoading(false);
//                    view.setFoundedRestaurants(restaurants);
//                }, throwable -> view.showError());
//    }

    public void loadRestaurants(int page, String keyword, String filterCuisines, String filterInstitutes) {
        view.showLoading(true);
        RepositoryProvider.getAppRepository().getRestaurants(1, keyword, filterCuisines, filterInstitutes, page, true)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(restaurants -> {
                    view.showLoading(false);
                    view.setData(restaurants);
                }, throwable -> view.showError());
    }

    public void loadSuggestions(String keyword, String filterCuisines, String filterInstitutes) {

        RepositoryProvider.getAppRepository().getRestaurants(1, keyword, filterCuisines, filterInstitutes, null, true)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(restaurants -> {
                    view.showLoading(false);
                    view.setSuggestion(restaurants);
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
                    view.showLoading(false);
                    view.setCusines(cusines);
                }, throwable -> view.showError());
    }

    private void loadData(int page, String keyword, String filterCuisines, String filterInstitutes) {
        view.showLoading(true);

        RepositoryProvider.getAppRepository().getInstitutions()
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(institutes -> {
                    view.showLoading(false);
                    view.setInstitutions(institutes);
                    loadRestaurants(page, keyword, filterCuisines, filterInstitutes);
                    loadNearRestaurants(width);
                }, throwable -> view.showError());

        loadCuisines();
    }
}
