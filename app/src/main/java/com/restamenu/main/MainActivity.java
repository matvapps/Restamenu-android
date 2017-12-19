package com.restamenu.main;

import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.restamenu.BuildConfig;
import com.restamenu.PopupDropDownAdapter;
import com.restamenu.PopupFilterItem;
import com.restamenu.R;
import com.restamenu.base.BaseNavigationActivity;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;
import com.restamenu.restaurant.RestaurantActivity;
import com.restamenu.util.Logger;
import com.squareup.picasso.Picasso;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseNavigationActivity<MainPresenter, MainView, List<Restaurant>>
        implements MainView, RestaurantClickListener, DiscreteScrollView.OnItemChangedListener, GravitySnapHelper.SnapListener {

    private View nearbyListContainer;
    private RecyclerView nearbyRestaurantsRecycler;
    private RecyclerView restaurantsRecycler;
    private NearbyRestaurantListAdapter nearbyRestaurantListAdapter;
    private RestaurantListAdapter restaurantListAdapter;
    private DiscreteScrollView nearbyRestaurantPicker;
    private ImageView nearbyContainerBackground;
    private View dropDownMenuCuisine;
    private View dropDownMenuInstitute;
    private PopupDropDownAdapter cuisinePopupDropdownAdapter;
    private PopupDropDownAdapter institutePopupDropdownAdapter;
    private PopupWindow cuisinePopup;
    private PopupWindow institutePopup;
    private PopupWindow popupWindow;

    @Override
    protected void initViews() {
        super.initViews();
        nearbyRestaurantsRecycler = findViewById(R.id.restaurant_list_container);
        restaurantsRecycler = findViewById(R.id.restaurants_list);
        nearbyListContainer = findViewById(R.id.nearby_list_container);
        nearbyListContainer.setVisibility(View.GONE);
        nearbyContainerBackground = findViewById(R.id.nearby_restaurants_container_background);
        dropDownMenuCuisine = findViewById(R.id.dropdown_menu_cuisine);
        dropDownMenuInstitute = findViewById(R.id.dropdown_menu_institute);

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
            cuisinePopupDropdownAdapter = new PopupDropDownAdapter();
            institutePopupDropdownAdapter = new PopupDropDownAdapter();

            dropDownMenuCuisine.setOnClickListener(this::displayCuisinePopupWindow);
            dropDownMenuInstitute.setOnClickListener(this::displayInstitutePopupWindow);

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

    private void displayCuisinePopupWindow(View anchorView) {
        cuisinePopup.showAsDropDown(anchorView);
    }

    private void displayInstitutePopupWindow(View anchorView) {
        institutePopup.showAsDropDown(anchorView);
    }

    @Override
    protected void attachPresenter() {
        Logger.log("Attach");
        if (presenter == null) {


            presenter = new MainPresenter(200);
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

            institutePopup = new PopupWindow(MainActivity.this);
            View layout = getLayoutInflater().inflate(R.layout.popup_content, null);

            TextView textView = layout.findViewById(R.id.dropdown_content_title);
            RecyclerView recyclerView = layout.findViewById(R.id.dropdown_content_grid);

            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            recyclerView.setAdapter(institutePopupDropdownAdapter);

            String titlePattern = getResources().getString(R.string.popup_dropdown_title);
            textView.setText(String.format(titlePattern, "institutions"));

            for (int i = 0; i < data.size(); i++) {
                institutePopupDropdownAdapter.addItem(new PopupFilterItem<>(data.get(i), false));
            }

            institutePopup.setContentView(layout);
            institutePopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            institutePopup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            institutePopup.setOutsideTouchable(true);
            institutePopup.setFocusable(true);
            institutePopup.setBackgroundDrawable(new BitmapDrawable());
        }
    }

    @Override
    public void setCusines(List<Cusine> cusines) {
        for (int i = 0; i < cusines.size(); i++)
            Logger.log(cusines.get(i).getName());

        if (isTablet()) {

            cuisinePopup = new PopupWindow(MainActivity.this);
            View layout = getLayoutInflater().inflate(R.layout.popup_content, null);

            TextView textView = layout.findViewById(R.id.dropdown_content_title);
            RecyclerView recyclerView = layout.findViewById(R.id.dropdown_content_grid);

            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            recyclerView.setAdapter(cuisinePopupDropdownAdapter);

            String titlePattern = getResources().getString(R.string.popup_dropdown_title);
            textView.setText(String.format(titlePattern, "cuisine"));

            for (int i = 0; i < cusines.size(); i++) {
                cuisinePopupDropdownAdapter.addItem(new PopupFilterItem<>(cusines.get(i), false));
            }

            cuisinePopup.setContentView(layout);
            cuisinePopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            cuisinePopup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            cuisinePopup.setOutsideTouchable(true);
            cuisinePopup.setFocusable(true);
            cuisinePopup.setBackgroundDrawable(new BitmapDrawable());

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
}
