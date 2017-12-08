package com.restamenu.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.restamenu.R;
import com.restamenu.model.content.Product;
import com.restamenu.restaurant.adapter.AdapterItemType;
import com.restamenu.restaurant.adapter.ItemType;
import com.restamenu.restaurant.adapter.RestaurantsAdapter;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_restaurant_test);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new RestaurantsAdapter();
        recycler.setAdapter(adapter);
        List<AdapterItemType> items = new ArrayList<>();
        items.add(new AdapterItemType("Menu", null, ItemType.TITLE));
        items.add(new AdapterItemType<List<Product>>(null, null, ItemType.MENU_PHONE));
        adapter.setItems(items);


    }
}
