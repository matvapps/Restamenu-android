package com.restamenu.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.base.BaseNavigationActivity;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;
import com.restamenu.restaurant.RestaurantActivity;
import com.restamenu.util.DimensionUtil;
import com.restamenu.util.Logger;
import com.restamenu.views.search.RestaurantsSearchView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.Pivot;
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
    private List<Institute> institutes;

    @Override
    protected void initViews() {
        super.initViews();
        nearbyRestaurantsRecycler = findViewById(R.id.restaurant_list_container);
        restaurantsRecycler = findViewById(R.id.restaurants_list);
        nearbyListContainer = findViewById(R.id.nearby_list_container);
        nearbyListContainer.setVisibility(View.GONE);
        nearbyContainerBackground = findViewById(R.id.nearby_restaurants_container_background);

        nearbyRestaurantListAdapter = new NearbyRestaurantListAdapter();

        searchView = findViewById(R.id.search_view);
        searchView.setSearchListener(this);
        searchView.showSearch(false);

        institutes = new ArrayList<>();


        if (!isTablet()) {
            nearbyRestaurantPicker = findViewById(R.id.nearby_restaurant_picker);
            nearbyRestaurantPicker.setOrientation(Orientation.HORIZONTAL);
            nearbyRestaurantPicker.addOnItemChangedListener(this);
            nearbyRestaurantPicker.setSlideOnFling(true);
            nearbyRestaurantPicker.setItemTransitionTimeMillis(430);
            nearbyRestaurantPicker.setItemTransformer(new ScaleTransformer.Builder()
                    .setMinScale(0.86f)
                    .setPivotX(Pivot.X.LEFT)
                    .setPivotY(Pivot.Y.CENTER)
                    .build());

        } else {
            new GravitySnapHelper(Gravity.START, false, this)
                    .attachToRecyclerView(nearbyRestaurantsRecycler);

            nearbyRestaurantsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            nearbyRestaurantsRecycler.setAdapter(nearbyRestaurantListAdapter);
        }

        restaurantsRecycler.setNestedScrollingEnabled(false);
        restaurantsRecycler.setHasFixedSize(false);

        if (isTablet()) {
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
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            //linearLayoutManager.setAutoMeasureEnabled(false);
            restaurantsRecycler.setLayoutManager(linearLayoutManager);
        }

        restaurantListAdapter = new RestaurantListAdapter(this);
        restaurantsRecycler.setAdapter(restaurantListAdapter);

    }

    @Override
    protected void attachPresenter() {
        Logger.log("Attach");
        if (presenter == null) {
            presenter = new MainPresenter(DimensionUtil.getScreenWidth(this));
        }
        presenter.attachView(this);
        presenter.init();

    }

    @Override
    protected boolean showToolbarBackStack() {
        return true;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void setData(@NonNull List<Restaurant> data) {
        Logger.log("Set restaurants: " + data.size());
        restaurantListAdapter.setData(data);
    }

    @Override
    public void setFoundedRestaurants(List<Restaurant> data) {
        Logger.log("Found: " + data.size() + " restaurant('s)");
        restaurantListAdapter = new RestaurantListAdapter(this);
        restaurantListAdapter.setData(data);
        restaurantListAdapter.setInstituteList(institutes);
        restaurantsRecycler.setAdapter(restaurantListAdapter);
    }

    @Override
    public void setSuggestion(List<Restaurant> data) {
        Logger.log("Found: " + data.toString());

        ArrayList<String> restNames = new ArrayList<>();

        for (Restaurant restaurant : data) {
            restNames.add(restaurant.getName());
        }

        searchView.setSuggestions(restNames);

    }

    @Override
    public void setNearRestaurants(List<Restaurant> data) {
        Logger.log("Set near restaurants: " + data.toString());


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

        nearbyListContainer.setVisibility(View.VISIBLE);

    }

    @Override
    public void setInstitutions(List<Institute> data) {
        nearbyRestaurantListAdapter.setInstituteList(data);
        restaurantListAdapter.setInstituteList(data);

        institutes = data;

        searchView.setInstitutions(data);
    }

    @Override
    public void setCusines(List<Cusine> cusines) {
        for (int i = 0; i < cusines.size(); i++)
            Logger.log(cusines.get(i).getName());

        searchView.setCuisines(cusines);
    }

    @Override
    public void showError() {
        super.showError();
        //TODO
    }

    @Override
    public void onRestaurantClicked(Integer id) {
        RestaurantActivity.start(MainActivity.this, id);
    }

    private void onItemChanged(Restaurant restaurant) {
        //load restaurant background
        String backgroundUrl = restaurant.getBackground() + BuildConfig.IMAGE_WIDTH_400;
        Glide.with(this)
                .load(BuildConfig.BASE_URL +
                        backgroundUrl.substring(1, backgroundUrl.length()))
                .into(nearbyContainerBackground);


    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    @Override
    public void onSnap(int position) {
        Logger.log("snapped item: " + position);
    }


    @Override
    public void finish() {
        // dismiss filter popup window if it's showing on phone
        if (!isTablet()) {
            if (searchView.getFilterPopup().isShowing()) {
                searchView.getFilterPopup().dismiss();

                return;
            }
        }

        super.finish();
    }


    /**
     * SearchListener
     */
    @Override
    public void onPerformSearch(List<PopupFilterItem> filterList, CharSequence searchString) {

        List<Cusine> cuisineFilterList = new ArrayList<>();
        List<Institute> instituteFilterList = new ArrayList<>();

        for (PopupFilterItem item : filterList) {
            Object obj = item.getItem();

            if (obj instanceof Cusine) {
                cuisineFilterList.add((Cusine) obj);
            } else if (obj instanceof Institute) {
                instituteFilterList.add((Institute) (obj));
            }
        }

        String filterCuisineParam = filterListToString(cuisineFilterList);
        String filterInstituteParam = filterListToString(instituteFilterList);
        String keyword = searchString.toString();

        if (!keyword.equals("") || !filterCuisineParam.equals("") || !filterInstituteParam.equals("")) {
            presenter.performSearch(keyword, filterCuisineParam, filterInstituteParam);
        }
    }

    @Override
    public void onInputDataChanged(List<PopupFilterItem> filterList, String keyword) {

        List<Cusine> cuisineFilterList = new ArrayList<>();
        List<Institute> instituteFilterList = new ArrayList<>();

        for (PopupFilterItem item : filterList) {
            Object obj = item.getItem();

            if (obj instanceof Cusine) {
                cuisineFilterList.add((Cusine) obj);
            } else if (obj instanceof Institute) {
                instituteFilterList.add((Institute) (obj));
            }
        }

        String filterCuisineParam = filterListToString(cuisineFilterList);
        String filterInstituteParam = filterListToString(instituteFilterList);


        if (!keyword.equals("") || !filterCuisineParam.equals("") || !filterInstituteParam.equals("")) {
            presenter.loadSuggestions(keyword, filterCuisineParam, filterInstituteParam);
        }
    }

    //    @Override
//    public void onInstituteChanged(PopupFilterItem institute) {
//
//    }
//
//    @Override
//    public void onCuisineChanged(PopupFilterItem cuisine) {
//
//    }

    private String filterListToString(List filterList) {
        StringBuilder result = new StringBuilder();

        if (filterList != null && filterList.size() > 0) {

            if (filterList.get(0) instanceof Cusine) {
                for (int i = 0; i < filterList.size(); i++) {
                    result.append(((Cusine) filterList.get(i)).getId());
                    if (i < filterList.size() - 1)
                        result.append(",");
                }
            } else if (filterList.get(0) instanceof Institute) {
                for (int i = 0; i < filterList.size(); i++) {
                    result.append(((Institute) filterList.get(i)).getId());
                    if (i < filterList.size() - 1)
                        result.append(",");
                }
            }

        }
        return result.toString();
    }

    /**
     * SearchListener
     */
}
