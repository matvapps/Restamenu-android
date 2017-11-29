package com.restamenu.main;

import com.restamenu.api.DataSource;
import com.restamenu.api.RepositoryProvider;
import com.restamenu.base.Presenter;
import com.restamenu.model.content.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roodie
 */

public class MainPresenter implements Presenter<MainView> {

    private MainView view;

    @Override
    public void attachView(MainView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void init() {
        List<Restaurant> restaurants = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            restaurants.add(new Restaurant());
        }
        view.setData(restaurants);

    }

    public void loadData() {
        RepositoryProvider.getRemoteRepository().getRestaurants(new DataSource.LoadRestaurantsCallback() {
            @Override
            public void onNext(List<Restaurant> data) {
                view.setData(data);
                view.showLoading(false);
            }

            @Override
            public void onError(Throwable error) {
                view.showError();

            }
        });
    }
}
