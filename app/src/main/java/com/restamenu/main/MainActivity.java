package com.restamenu.main;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.viewanimator.ViewAnimator;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.base.BaseNavigationActivity;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;
import com.restamenu.restaurant.RestaurantActivity;
import com.restamenu.util.AndroidUtils;
import com.restamenu.util.Logger;
import com.restamenu.views.custom.CustomSwipeToRefresh;
import com.restamenu.views.dialog.NoInternetDialog;
import com.restamenu.views.recycler.EndlessRecyclerOnScrollListener;
import com.restamenu.views.search.RestaurantsSearchView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseNavigationActivity<MainPresenter, MainView, List<Restaurant>>
        implements MainView, RestaurantClickListener, DiscreteScrollView.OnItemChangedListener, GravitySnapHelper.SnapListener,
        RestaurantsSearchView.SearchListener, SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {

    private View nearbyListContainer;
    private View nearBackContainer;
    private RecyclerView nearbyRestaurantsRecycler;
    private RecyclerView restaurantsRecycler;
    private NearbyRestaurantListAdapter nearbyRestaurantListAdapter;
    private RestaurantListAdapter restaurantListAdapter;
    private DiscreteScrollView nearbyRestaurantPicker;
    private ImageView nearbyContainerBackground;
    private RestaurantsSearchView searchView;
    private List<Institute> institutes;
    private CustomSwipeToRefresh swipeRefreshLayout;
    private AppBarLayout appBarLayout;
    private TextView filterQuantText;
    private View filterQuantContainer;
    private View filterContainer;
    private View logo;
    private FrameLayout searchContainer;

    private String keyword = null;
    private String filterCuisines = null;
    private String filterInstitutes = null;

    private int currentNearRestaurantIndex = -1;
    private int currentAppBarOffset;

    private final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

//            nearBackContainer.setBackgroundColor(Color.BLACK);
            ViewAnimator
                    .animate(nearbyContainerBackground)
                    .duration(1200)
                    .interpolator(new AccelerateInterpolator())
                    .alpha(1, 0.0f)
                    .onStop(() -> {
                        nearbyContainerBackground.setImageBitmap(bitmap);
                    })
                    .thenAnimate(nearbyContainerBackground)
                    .duration(900)
                    .interpolator(new AccelerateInterpolator())
                    .alpha(0.0f, 1)
                    .start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

//    private final SimpleTarget simpleTarget = new SimpleTarget<Drawable>() {
//        @Override
//        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//            ViewAnimator
//                    .animate(nearbyContainerBackground)
//                    .duration(5000)
//                    .interpolator(new LinearInterpolator())
//                    .alpha(1, 0.35f)
//                    .onStop(() -> {
//                        Toast.makeText(MainActivity.this, "onStop", Toast.LENGTH_SHORT).show();
//                        nearbyContainerBackground.setAlpha(0.35f);
//                        nearbyContainerBackground.setImageDrawable(resource);
//                    })
//                    .thenAnimate(nearbyContainerBackground)
//                    .duration(2000)
//                    .interpolator(new LinearInterpolator())
//                    .alpha(0.35f, 1)
//                    .start();
//        }
//    };

    private int currentPage = 1;

    private boolean hasOtherPages;

    @Override
    protected void initViews() {
        super.initViews();
        nearBackContainer = findViewById(R.id.nearby_under_content);
        nearbyRestaurantsRecycler = findViewById(R.id.restaurant_list_container);
        restaurantsRecycler = findViewById(R.id.restaurants_list);
        nearbyListContainer = findViewById(R.id.nearby_list_container);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        nearbyContainerBackground = findViewById(R.id.nearby_restaurants_container_background);
        appBarLayout = findViewById(R.id.appbar_layout);
        filterContainer = findViewById(R.id.action_choose_filter);
        filterQuantText = findViewById(R.id.filter_quantity);
        logo = findViewById(R.id.logo);
        searchContainer = findViewById(R.id.search_container);


        swipeRefreshLayout.setOnRefreshListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
        nearbyListContainer.setVisibility(View.GONE);
        nearbyRestaurantListAdapter = new NearbyRestaurantListAdapter();


        searchView = findViewById(R.id.search_view);
        searchView.setSearchListener(this);
        searchView.showSearch(false);

        institutes = new ArrayList<>();

        logo.setOnClickListener(view -> scrollToTop());

        if (!isTablet()) {
            filterQuantContainer = findViewById(R.id.filter_quant_container);
            filterQuantText.setText("0");
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
            nearbyRestaurantPicker.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
                @Override
                public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

                }

                @Override
                public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {
                    onItemChanged(adapterPosition);
                }

                @Override
                public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {

                }
            });

        } else {
            new GravitySnapHelper(Gravity.START, false, this)
                    .attachToRecyclerView(nearbyRestaurantsRecycler);

            nearbyRestaurantsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            nearbyRestaurantsRecycler.setAdapter(nearbyRestaurantListAdapter);
        }

        if (isTablet()) {
            final int span_count = getResources().getInteger(R.integer.restaurant_span_count);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, span_count);

//            int itemPadding = getResources().getDimensionPixelSize(R.dimen.land_rest_card_padding);
//            GridSpacingItemDecoration spacingItemDecoration = new GridSpacingItemDecoration(span_count, itemPadding, itemPadding, false);

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
//            restaurantsRecycler.addItemDecoration(spacingItemDecoration);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            restaurantsRecycler.setLayoutManager(linearLayoutManager);
        }
        restaurantListAdapter = new RestaurantListAdapter(this);
        restaurantsRecycler.setAdapter(restaurantListAdapter);
        restaurantsRecycler.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadNextPage(View view) {
                if (hasOtherPages) {
                    hasOtherPages = false;
                    currentPage++;
                    presenter.loadRestaurants(currentPage, keyword, filterCuisines, filterInstitutes);
                }
            }
        });

    }

    @Override
    protected void attachPresenter() {
        Logger.log("Attach");
        if (presenter == null) {
            presenter = new MainPresenter(AndroidUtils.getScreenWidth(this));
        }
        presenter.attachView(this);
        filterQuantContainer = findViewById(R.id.filter_quant_container);
        presenter.init(currentPage);

        noInternetDialog.setNoInternetDialogListener(new NoInternetDialog.NoInternetDialogListener() {
            @Override
            public void onDismiss() {
                if (noInternetDialog.isRefreshing()) {
                    presenter.init(currentPage);
                    noInternetDialog.setRefreshing(false);
                } else {
                    if (!doubleBackToExitPressedOnce)
                        presenter.init(currentPage);

                    onBackPressed();
                }



            }

            @Override
            public void onRefresh() {
                presenter.init(currentPage);
            }
        });
    }

    @Override
    protected boolean showToolbarBackStack() {
        return true;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_test_main;
    }

    @Override
    public void setData(List<Restaurant> data) {
        Logger.log("loaded " + data.size() + " restaurants");

        hasOtherPages = true;
        if (currentPage == 1)
            restaurantListAdapter.setItems(data);
        else
            restaurantListAdapter.addItems(data);
    }

    @Override
    public void setFoundedRestaurants(List<Restaurant> data) {
        Logger.log("Found :" + data.size() + " restaurants");
        restaurantListAdapter = new RestaurantListAdapter(this);
        restaurantListAdapter.setItems(data);
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
        Logger.log("Near: " + data.toString());


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
            onItemChanged(0);
        } else {
            onItemChanged(1);
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
    }

    @Override
    public void onRestaurantClicked(Integer id) {
        RestaurantActivity.start(MainActivity.this, id);
    }

    private void onItemChanged(int position) {
        Restaurant restaurant = nearbyRestaurantListAdapter.getItem(position);

        if (currentNearRestaurantIndex != position) {
            currentNearRestaurantIndex = position;
            //load restaurant background
            String backgroundUrl = restaurant.getBackground() + BuildConfig.IMAGE_WIDTH_400;
            Picasso.with(this)
                    .load(BuildConfig.BASE_URL + backgroundUrl.substring(1, backgroundUrl.length()))
                    .resize(800, 800).centerInside()
                    .into(target);
        }

    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    @Override
    public void onSnap(int position) {
        Logger.log("snapped item: " + position);
        if (position == 0)
            position = 1;

        onItemChanged(position);
    }

//    @Override
//    public void finish() {
//
//        // dismiss filter popup window if it's showing on phone
//        if (!isTablet()) {
//            if (searchView.getFilterPopup().isShowing()) {
//                searchView.getFilterPopup().dismiss();
//                return;
//            }
//        }
//
//        super.finish();
//    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }


//        if (!loadingView.isShow()) {

            this.doubleBackToExitPressedOnce = true;

            if (!isTablet()) {
                if (searchView.getFilterPopup().isShowing()) {
                    searchView.getFilterPopup().dismiss();
                    this.doubleBackToExitPressedOnce = false;
                    super.onBackPressed();
                    return;
                }
            }


            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
//        }
    }


    /**
     * SearchListener
     */
    @Override
    public void onPerformSearch(List<CheckedItem> filterList, CharSequence searchString) {

        List<Cusine> cuisineFilterList = new ArrayList<>();
        List<Institute> instituteFilterList = new ArrayList<>();

        for (CheckedItem item : filterList) {
            Object obj = item.getItem();

            if (obj instanceof Cusine) {
                cuisineFilterList.add((Cusine) obj);
            } else if (obj instanceof Institute) {
                instituteFilterList.add((Institute) (obj));
            }
        }

        filterCuisines = filterListToString(cuisineFilterList);
        filterInstitutes = filterListToString(instituteFilterList);
        keyword = searchString.toString();

        currentPage = 1;
        restaurantListAdapter.clearItems();
        presenter.loadRestaurants(currentPage, keyword, filterCuisines, filterInstitutes);

    }

    @Override
    public void popupOpened() {
        if (isTablet()) {
            showTransparentView(true);
        }
    }

    @Override
    public void popupClosed() {
        if (isTablet())
            showTransparentView(false);
    }

    @Override
    public void searchViewClicked() {
        if (isTablet()) {
            appBarLayout.offsetTopAndBottom(-searchContainer.getTop() - currentAppBarOffset);
        }
    }

    @Override
    public void onInputDataChanged(List<CheckedItem> filterList, String keyword) {

        List<Cusine> cuisineFilterList = new ArrayList<>();
        List<Institute> instituteFilterList = new ArrayList<>();

        for (CheckedItem item : filterList) {
            Object obj = item.getItem();

            if (obj instanceof Cusine) {
                cuisineFilterList.add((Cusine) obj);
            } else if (obj instanceof Institute) {
                instituteFilterList.add((Institute) (obj));
            }
        }

        String filterCuisineParam = filterListToString(cuisineFilterList);
        String filterInstituteParam = filterListToString(instituteFilterList);

        if (!isTablet())
            if (filterList.size() != 0) {
                filterQuantText.setText(String.valueOf(filterList.size()));
                filterQuantContainer.setVisibility(View.VISIBLE);
                filterContainer.setPadding(0, 0, 0, 0);

            } else if (filterQuantContainer != null) {
                filterQuantContainer.setVisibility(View.GONE);
                filterContainer.setPadding((int) getResources().getDimension(R.dimen.search_view_filter_text_margin_start), 0, 0, 0);
            }

        if (!keyword.equals("") || !filterCuisineParam.equals("") || !filterInstituteParam.equals("")) {
            presenter.loadSuggestions(keyword, filterCuisineParam, filterInstituteParam);
        }
    }

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

    @Override
    public void onRefresh() {
        currentPage = 1;
        presenter.init(currentPage);
    }

    /**
     * SearchListener
     */

    public void scrollToTop() {
        restaurantsRecycler.scrollToPosition(0);
        appBarLayout.setExpanded(true);
    }

    @Override
    public void showLoading(boolean show) {
        super.showLoading(show);

        if (!show) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        currentAppBarOffset = offset;

        if (offset == 0) {
            // Fully expanded
            swipeRefreshLayout.setEnabled(true);
        } else {
            // Not fully expanded or collapsed
            swipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        super.onNetworkConnectionChanged(isConnected);
        if (isConnected) {
            if (noInternetDialog.isShown()) {
                noInternetDialog.setRefreshing(true);
                noInternetDialog.dismiss();
            }

            presenter.init(currentPage);
        }
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Logger.log("Button back pressed");
//
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//
//            return false;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

}
