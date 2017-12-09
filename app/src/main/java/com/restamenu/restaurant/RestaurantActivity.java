package com.restamenu.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.restamenu.R;
import com.restamenu.base.BasePresenterActivity;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Promotion;
import com.restamenu.model.content.Restaurant;
import com.restamenu.restaurant.adapter.RestaurantsAdapter;
import com.restamenu.util.Logger;

import java.util.List;

public class RestaurantActivity extends BasePresenterActivity<RestaurantsPresenter, RestaurantView, Restaurant> implements RestaurantView {

    public static final String KEY_RESTAURANT_ID = "key_rest_id";
    private Integer restaurantId;
    private RestaurantsAdapter adapter;
    private RecyclerView recycler;

    public static void start(@NonNull Activity activity, @NonNull Integer restaurantId) {
        Intent intent = new Intent(activity, RestaurantActivity.class);
        intent.putExtra(KEY_RESTAURANT_ID, restaurantId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restaurantId = getIntent().getIntExtra(KEY_RESTAURANT_ID, 1);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setData(@NonNull Restaurant data) {
        Logger.log("Rest: " + data.toString());

    }

    @Override
    public void setCategories(@NonNull List<Category> categories) {
        Logger.log("Categories: " + categories.toString());
    }

    @Override
    public void setGallery(@NonNull List<Image> images) {
        Logger.log("Gallery: " + images.toString());
    }

    @Override
    public void setPromotions(@NonNull List<Promotion> promotions) {
        Logger.log("Promotions: " + promotions.toString());
    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    protected void attachPresenter() {
        Logger.log("Attach");
        if (presenter == null) {
            presenter = new RestaurantsPresenter(restaurantId);
        }
        presenter.attachView(this);
        presenter.loadData();

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_restaurant_test);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new RestaurantsAdapter();
        recycler.setAdapter(adapter);
       /* List<AdapterItemType> items = new ArrayList<>();
        items.add(new AdapterItemType("Menu", null, ItemType.TITLE));
        items.add(new AdapterItemType<List<Product>>(null, null, ItemType.MENU_PHONE));
        adapter.setItems(items);*/

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_restaurant_test;
    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void hideEmptyView() {

    }
}
