package com.restamenu.main;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.github.florent37.viewanimator.ViewAnimator;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.base.BaseNavigationActivity;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;
import com.restamenu.restaurant.RestaurantActivity;
import com.restamenu.util.Logger;
import com.restamenu.views.search.RestaurantsSearchView;
import com.squareup.picasso.Picasso;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseNavigationActivity<MainPresenter, MainView, List<Restaurant>>
        implements MainView, RestaurantClickListener, DiscreteScrollView.OnItemChangedListener, GravitySnapHelper.SnapListener, RestaurantsSearchView.SearchListener {

    private View nearbyListContainer;
    private RecyclerView nearbyRestaurantsRecycler;
    private RecyclerView restaurantsRecycler;
    private NearbyRestaurantListAdapter nearbyRestaurantListAdapter;
    private RestaurantListAdapter restaurantListAdapter;
    private DiscreteScrollView nearbyRestaurantPicker;
    private ImageView nearbyContainerBackground;
    private RestaurantsSearchView searchView;


    @Override
    protected void initViews() {
        super.initViews();
        nearbyRestaurantsRecycler = findViewById(R.id.restaurant_list_container);
        restaurantsRecycler = findViewById(R.id.restaurants_list);
        nearbyListContainer = findViewById(R.id.nearby_list_container);
        nearbyListContainer.setVisibility(View.GONE);
        nearbyContainerBackground = findViewById(R.id.nearby_restaurants_container_background);

        nearbyRestaurantListAdapter = new NearbyRestaurantListAdapter(MainActivity.this);

        if (!isTablet()) {
            nearbyRestaurantPicker = findViewById(R.id.nearby_restaurant_picker);
            nearbyRestaurantPicker.setOrientation(Orientation.HORIZONTAL);
            nearbyRestaurantPicker.addOnItemChangedListener(this);
            nearbyRestaurantPicker.setSlideOnFling(true);
            nearbyRestaurantPicker.setItemTransitionTimeMillis(430);
            nearbyRestaurantPicker.setItemTransformer(new ScaleTransformer.Builder()
                    .setMinScale(0.85f)
                    .build());

        } else {
            searchView = findViewById(R.id.search_view);
            searchView.setSearchListener(this);
            searchView.showSearch(false);

            new GravitySnapHelper(Gravity.START, false, this)
                    .attachToRecyclerView(nearbyRestaurantsRecycler);

            nearbyRestaurantsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            nearbyRestaurantsRecycler.setAdapter(nearbyRestaurantListAdapter);
        }

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
        restaurantListAdapter = new RestaurantListAdapter(MainActivity.this, this);
        restaurantsRecycler.setAdapter(restaurantListAdapter);


    }

    @Override
    protected void attachPresenter() {
        Logger.log("Attach");
        if (presenter == null) {

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            int width = size.x;

            presenter = new MainPresenter(width);
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
    public void setSuggestion(List<Restaurant> data) {
        searchView.setSuggestions(data);
    }

    @Override
    public void setNearRestaurants(List<Restaurant> data) {
        Logger.log("Near: " + data.toString());

        nearbyListContainer.setVisibility(View.VISIBLE);

        if (isTablet()) {
            List<Restaurant> restaurants = new ArrayList<>();
            nearbyRestaurantListAdapter.setUseScrollIt(true);
            restaurants.add(new Restaurant());
            restaurants.addAll(data);
            data = restaurants;

        }

        nearbyRestaurantListAdapter.setData(data);


        if (!isTablet()) {
            nearbyRestaurantPicker.setAdapter(nearbyRestaurantListAdapter);
            onItemChanged(nearbyRestaurantListAdapter.getItem(0));
        } else {
            onItemChanged(nearbyRestaurantListAdapter.getItem(1));
        }
    }

    @Override
    public void setInstitutions(List<Institute> data) {
        nearbyRestaurantListAdapter.setInstituteList(data);
        restaurantListAdapter.setInstituteList(data);

        if (isTablet()) {
            searchView.setInstitutions(data);
        }
    }

    @Override
    public void setCusines(List<Cusine> cusines) {
        for (int i = 0; i < cusines.size(); i++)
            Logger.log(cusines.get(i).getName());

        if (isTablet()) {
            searchView.setCuisines(cusines);
        }
    }

//    public void initPopup(List<Object> data) {
//        popupWindow = new PopupWindow(MainActivity.this);
//        View popupLayout;
//        View layout = getLayoutInflater().inflate(R.layout.popup_content, null);
//
//        TextView textView = layout.findViewById(R.id.dropdown_content_title);
//        RecyclerView recyclerView = layout.findViewById(R.id.dropdown_content_grid);
//
//        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
//
//
//        if (isTablet()) {
//        } else {
//
//        }
//
//    }

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

    private void onItemChanged(Restaurant restaurant) {
        //load restaurant background
        String backgroundUrl = restaurant.getBackground();
        Picasso.with(this)
                .load(BuildConfig.BASE_URL +
                        backgroundUrl.substring(1, backgroundUrl.length()))
                .into(nearbyContainerBackground, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        ViewAnimator
                                // Splash alpha animation
                                .animate(nearbyContainerBackground)
                                .startDelay(0)
                                .duration(2000)
                                .interpolator(new LinearInterpolator())
                                .alpha(0.2f, 1)
                                .start();
                    }

                    @Override
                    public void onError() {
                        //do smth when there is picture loading error
                    }
                });


    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

        ViewAnimator
                // Splash alpha animation
                .animate(nearbyContainerBackground)
                .startDelay(0)
                .duration(700)
                .interpolator(new LinearInterpolator())
                .alpha(1f, 0)
                .start();

        onItemChanged(nearbyRestaurantListAdapter.getItem(adapterPosition));

    }

    @Override
    public void onSnap(int position) {
        Logger.log("snapped item: " + position);
        //load restaurant background

        // if  'scroll it' item
        if (position == 0)
            position = 1;

        onItemChanged(nearbyRestaurantListAdapter.getItem(position));
    }

    /**
     * SearchListener
     */
    @Override
    public void onPerformSearch(CharSequence searchString) {
        presenter.performSearch(searchString.toString());
    }

    @Override
    public void onInstituteChanged(int instituteId) {

    }

    @Override
    public void inCuisineChanged(int cuisineId) {
    }
    /**
     * SearchListener
     */
}
