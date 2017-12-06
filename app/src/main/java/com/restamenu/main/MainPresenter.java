package com.restamenu.main;

import com.restamenu.api.DataSource;
import com.restamenu.api.RepositoryProvider;
import com.restamenu.base.Presenter;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;

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
        loadData();
        //List<Restaurant> restaurants = new ArrayList<>();

       /* for (int i = 0; i < 10; i++) {
            restaurants.add(new Restaurant());
        }*/
        //view.setData(restaurants);

    }

    public void loadData() {
        view.showLoading(true);
        RepositoryProvider.getAppRepository().getRestaurants(1, new DataSource.LoadRestaurantsCallback() {
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

        RepositoryProvider.getAppRepository().getInstitutions(new DataSource.GetInstitutionsCallback() {
            @Override
            public void onNext(List<Institute> data) {

            }

            @Override
            public void onError(Throwable error) {

            }
        });


        RepositoryProvider.getAppRepository().getNearRestaurants(1, "56.8457373,60.5972259", new DataSource.LoadRestaurantsCallback() {
            @Override
            public void onNext(List<Restaurant> data) {
                view.setNearRestaurants(data);
            }

            @Override
            public void onError(Throwable error) {
                //TODO
            }
        });

    }
}
