package com.restamenu.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.restamenu.NearbyRestaurantListAdapter;
import com.restamenu.R;
import com.restamenu.StartSnapHelper;
import com.restamenu.base.BaseNavigationActivity;
import com.restamenu.model.content.Restaurant;

import java.util.List;

public class MainActivity extends BaseNavigationActivity<MainPresenter, MainView, List<Restaurant>> implements MainView {

    private RecyclerView nearbyRestaurantsView;
    private NearbyRestaurantListAdapter restaurantListAdapter;


    @Override
    protected void initViews() {
        super.initViews();
        nearbyRestaurantsView = findViewById(R.id.restaurant_list_container);
        StartSnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(nearbyRestaurantsView);

        nearbyRestaurantsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        restaurantListAdapter = new NearbyRestaurantListAdapter(this);
        nearbyRestaurantsView.setAdapter(restaurantListAdapter);
    }

    @Override
    protected void attachPresenter() {
        if (presenter == null) {
            presenter = new MainPresenter();
        }
        presenter.attachView(this);
        presenter.init();

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void setData(@NonNull List<Restaurant> data) {
        restaurantListAdapter.setData(data);
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
