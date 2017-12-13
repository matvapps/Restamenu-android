package com.restamenu.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.exblr.dropdownmenu.DropdownListItem;
import com.exblr.dropdownmenu.DropdownMenu;
import com.restamenu.R;
import com.restamenu.SpinnerGridViewAdapter;
import com.restamenu.StartSnapHelper;
import com.restamenu.base.BaseNavigationActivity;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;
import com.restamenu.restaurant.RestaurantActivity;
import com.restamenu.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseNavigationActivity<MainPresenter, MainView, List<Restaurant>> implements MainView, RestaurantClickListener {

    private View nearbyListContainer;
    private RecyclerView nearbyRestaurantsRecycler;
    private RecyclerView restaurantsRecycler;
    private RecyclerView itemTypeView;
    private NearbyRestaurantListAdapter nearbyRestaurantListAdapter;
    private RestaurantListAdapter restaurantListAdapter;


    @Override
    protected void initViews() {
        super.initViews();
        nearbyRestaurantsRecycler = findViewById(R.id.restaurant_list_container);
        restaurantsRecycler = findViewById(R.id.restaurants_list);
        nearbyListContainer = findViewById(R.id.nearby_list_container);
        DropdownMenu searchSpinner = findViewById(R.id.dropdown_menu_filter);
        nearbyListContainer.setVisibility(View.GONE);

        ArrayList list2 = createMockList(15, true);
        SpinnerGridViewAdapter mGridViewAdapter = new SpinnerGridViewAdapter(this, list2);

        View customContentView = getLayoutInflater().inflate(R.layout.spinner_content, null, false);
        GridView gridView = customContentView.findViewById(R.id.content);
        gridView.setAdapter(mGridViewAdapter);

        searchSpinner.add("Menu 2", customContentView);



        final StartSnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(nearbyRestaurantsRecycler);
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

        restaurantsRecycler.setLayoutManager(gridLayoutManager);
        nearbyRestaurantsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nearbyRestaurantListAdapter = new NearbyRestaurantListAdapter(MainActivity.this);
        nearbyRestaurantListAdapter.setData(new ArrayList<>());
        nearbyRestaurantsRecycler.setAdapter(nearbyRestaurantListAdapter);

        restaurantListAdapter = new RestaurantListAdapter(MainActivity.this, this);
        restaurantsRecycler.setAdapter(restaurantListAdapter);

    }


    private ArrayList createMockList(int count, boolean hasEmpty) {
        ArrayList list = new ArrayList();
        if (hasEmpty) {
            list.add(new DropdownListItem(0, "Hello", true, true));
        }
        for (int i = 1; i <= count; i++) {
            list.add(new DropdownListItem(10 + i, "Item-1-" + i));
        }
        return list;
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

        nearbyListContainer.setVisibility(View.VISIBLE);
        nearbyRestaurantListAdapter.setData(data);

    }

    @Override
    public void setInstitutions(List<Institute> data) {
        restaurantListAdapter.setInstituteList(data);
        nearbyRestaurantListAdapter.setInstituteList(data);
    }

    @Override
    public void showError() {
//        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        //TODO
    }

    @Override
    public void showLoading(boolean show) {
        //TODO
    }

    @Override
    public void onRestaurantClicked(Integer id) {
        RestaurantActivity.start(MainActivity.this, id);
    }
}
