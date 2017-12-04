package com.restamenu.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.restamenu.NearbyRestaurantListAdapter;
import com.restamenu.R;
import com.restamenu.RestaurantListAdapter;
import com.restamenu.StartSnapHelper;
import com.restamenu.base.BaseNavigationActivity;
import com.restamenu.model.content.Restaurant;
import com.restamenu.util.Logger;

import java.util.List;

public class MainActivity extends BaseNavigationActivity<MainPresenter, MainView, List<Restaurant>> implements MainView {

    private RecyclerView nearbyRestaurantsView;
    private RecyclerView restaurantsListView;
    private NearbyRestaurantListAdapter nearbyRestaurantListAdapter;
    private RestaurantListAdapter restaurantListAdapter;


    @Override
    protected void initViews() {
        super.initViews();
        nearbyRestaurantsView = findViewById(R.id.restaurant_list_container);
        restaurantsListView = findViewById(R.id.restaurants_list);

        final StartSnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(nearbyRestaurantsView);

        final int span_count = getResources().getInteger(R.integer.restaurant_span_count);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, span_count);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 1 && span_count > 2){
                    return  2;
                }
                return 1;
            }
        });

        restaurantsListView.setLayoutManager(gridLayoutManager);
        nearbyRestaurantsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nearbyRestaurantListAdapter = new NearbyRestaurantListAdapter();
        nearbyRestaurantsView.setAdapter(restaurantListAdapter);

        restaurantListAdapter = new RestaurantListAdapter();
        restaurantsListView.setAdapter(restaurantListAdapter);

    }


    @Override
    protected void attachPresenter() {
        Logger.log("Attach");
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
        Logger.log("Amount: " + data.size());
        restaurantListAdapter.setData(data);
    }

    @Override
    public void setNearRestaurants(List<Restaurant> data) {
        Logger.log("Amount: " + data.size());
        nearbyRestaurantListAdapter.setData(data);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        //TODO
    }

    @Override
    public void showLoading(boolean show) {
        //TODO
    }


}
