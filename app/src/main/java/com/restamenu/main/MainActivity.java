package com.restamenu.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.restamenu.NearbyRestaurantListAdapter;
import com.restamenu.R;
import com.restamenu.Restaurant;
import com.restamenu.StartSnapHelper;
import com.restamenu.base.BaseNavigationActivity;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseNavigationActivity<MainPresenter, MainView, List<Object>> implements MainView {

    RecyclerView nearbyRestaurantsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        nearbyRestaurantsView = findViewById(R.id.restaurant_list_container);
        StartSnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(nearbyRestaurantsView);

        nearbyRestaurantsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Restaurant> restaurants = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            restaurants.add(new Restaurant());
        }

        NearbyRestaurantListAdapter restaurantListAdapter = new NearbyRestaurantListAdapter(this, restaurants);

        nearbyRestaurantsView.setAdapter(restaurantListAdapter);

    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

    @Override
    protected void attachPresenter() {
        if (presenter == null) {
            presenter = new MainPresenter(getLoaderManager());
        }
        presenter.attachView(this);

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void setData(@NonNull List<Object> data) {
        //TODO
    }

    @Override
    public void showError() {
        //TODO
    }

    @Override
    public void showLoading(boolean show) {
        //TODO
    }


}
