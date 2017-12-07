package com.restamenu.main;

import com.restamenu.api.RepositoryProvider;
import com.restamenu.base.Presenter;
import com.restamenu.rx.BaseSchedulerProvider;
import com.restamenu.rx.SchedulerProvider;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;

import io.reactivex.Flowable;
import java.util.List;

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
        //List<Restaurant> restaurants = new ArrayList<>();

       /* for (int i = 0; i < 10; i++) {
            restaurants.add(new Restaurant());
        }*/
        //view.setData(restaurants);

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
    }
}
