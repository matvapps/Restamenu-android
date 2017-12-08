package com.restamenu.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.restamenu.NearbyRestaurantListAdapter;
import com.restamenu.R;
import com.restamenu.RestaurantListAdapter;
import com.restamenu.SampleItemType;
import com.restamenu.SectionsItemType;
import com.restamenu.StartSnapHelper;
import com.restamenu.base.BaseNavigationActivity;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;
import com.restamenu.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseNavigationActivity<MainPresenter, MainView, List<Restaurant>> implements MainView {

    private RecyclerView nearbyRestaurantsView;
    private RecyclerView restaurantsListView;
    private RecyclerView itemTypeView;
    private NearbyRestaurantListAdapter nearbyRestaurantListAdapter;
    private RestaurantListAdapter restaurantListAdapter;


    @Override
    protected void initViews() {
        super.initViews();
        nearbyRestaurantsView = findViewById(R.id.restaurant_list_container);
        restaurantsListView = findViewById(R.id.restaurants_list);

        itemTypeView = findViewById(R.id.item_type);
        itemTypeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ItemAdapter<SampleItemType> sampleItemTypeItemAdapter = new ItemAdapter<>();
        FastAdapter fastAdapter = FastAdapter.with(sampleItemTypeItemAdapter);

        itemTypeView.setAdapter(fastAdapter);

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("Category 1"));
        categories.add(new Category("Category 2"));
        categories.add(new Category("Category 3"));
        SectionsItemType sectionsItemType = new SectionsItemType(categories);

//        ArrayList<Promotion> promotions = new ArrayList<>();
//        promotions.add(new Promotion());
//        promotions.add(new Promotion());
//        promotions.add(new Promotion());
//        PromotionsItemType promotionsItemType = new PromotionsItemType(promotions);

//        sampleItemTypeItemAdapter.add(promotionsItemType);
        sampleItemTypeItemAdapter.add(sectionsItemType);


        final StartSnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(nearbyRestaurantsView);

        final int span_count = getResources().getInteger(R.integer.restaurant_span_count);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, span_count);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (restaurantListAdapter.getItem(position).getType()) {
                    case 0:
                        return 1;
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    case 3:
                        return 3;
                    default:
                        return -1;
                }
            }
        });

        restaurantsListView.setLayoutManager(gridLayoutManager);
        nearbyRestaurantsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nearbyRestaurantListAdapter = new NearbyRestaurantListAdapter(MainActivity.this);
        nearbyRestaurantsView.setAdapter(nearbyRestaurantListAdapter);

        restaurantListAdapter = new RestaurantListAdapter(MainActivity.this);
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
        Logger.log("Near: " + data.toString());
        nearbyRestaurantListAdapter.setData(data);
    }

    @Override
    public void setInstitutions(List<Institute> data) {
        //TODO
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
