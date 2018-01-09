package com.restamenu.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.restamenu.R;
import com.restamenu.base.BasePresenterActivity;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Product;
import com.restamenu.util.Logger;

import java.util.List;

public class CategoryActivity extends BasePresenterActivity<CategoryPresenter, CategoryView, List<Category>> implements CategoryView, View.OnClickListener {

    public static final String KEY_RESTAURANT_ID = "key_rest_id";
    public static final String KEY_SERVICE_ID = "key_service_id";
    public static final String KEY_CATEGORY_ID = "key_category_id";
    public static final String KEY_RESTAURANT_TITLE = "key_restaurant_title";

    private final int NEXT_CATEGORY = 1;
    private final int PREV_CATEGORY = -1;

    private Integer restaurantId;
    private Integer serviceId;
    private Integer categoryId;

    private int categoryIndex = 0;

    private ProductAdapter productAdapter;

    private ImageView btnTopCategoryPrevious;
    private ImageView btnTopCategoryNext;
    private TextView txtCategoryName;
    private RecyclerView productsRecycler;
    private View btnBottomCategoryPrevious;
    private View btnBottomCategoryNext;
    private TextView prevCategoryName;
    private TextView nextCategoryName;
    private NestedScrollView nestedScrollView;
    private TextView toolbarRestaurantTitle;
    private EditText findEditText;
    private View buttonFind;
    private View toolbarBack;

    private String restaurantTitle;

    private List<Category> categories;


    public static void start(@NonNull Context context, @NonNull Integer restaurantId,
                             @NonNull Integer serviceId, @NonNull Integer categoryId, String restaurantTitle) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(KEY_SERVICE_ID, serviceId);
        intent.putExtra(KEY_RESTAURANT_ID, restaurantId);
        intent.putExtra(KEY_CATEGORY_ID, categoryId);
        intent.putExtra(KEY_RESTAURANT_TITLE, restaurantTitle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restaurantId = getIntent().getIntExtra(KEY_RESTAURANT_ID, 1);
        serviceId = getIntent().getIntExtra(KEY_SERVICE_ID, 1);
        categoryId = getIntent().getIntExtra(KEY_CATEGORY_ID, 1);
        restaurantTitle = getIntent().getStringExtra(KEY_RESTAURANT_TITLE);

        super.onCreate(savedInstanceState);

        toolbarRestaurantTitle.setText(restaurantTitle);

    }


    @Override
    public void setProducts(List<Product> products) {
        Logger.log("Products : " + products.toString());

        productAdapter.setItems(products);

    }

    public void changeCategoryTo(int categoryId) {
        Logger.log("categoryIndex = " + categoryIndex);
        txtCategoryName.setText(categories.get
                (getCategoryIndex(categories, categoryId)).getName());


        presenter.loadData(categoryId);
    }

    @Override
    public void setData(@NonNull List<Category> data) {
        Logger.log("Categories: " + data.toString());
        categories = data;

        nestedScrollView.smoothScrollTo(0, 0);

        categoryIndex = getCategoryIndex(data, categoryId);
        txtCategoryName.setText(data.get(categoryIndex).getName());


        btnTopCategoryPrevious.setOnClickListener(this);
        btnTopCategoryNext.setOnClickListener(this);

        if (isTablet()) {
            btnBottomCategoryPrevious.setOnClickListener(this);
            btnTopCategoryNext.setOnClickListener(this);
        }

        if (categoryIndex == (data.size() - 1)) {

//            btnTopCategoryNext.setEnabled(false);
//            btnTopCategoryNext.setVisibility(View.INVISIBLE);

            btnTopCategoryPrevious.setEnabled(true);
            btnTopCategoryPrevious.setVisibility(View.VISIBLE);


            if (isTablet()) {
                prevCategoryName.setText(data.get(categoryIndex + PREV_CATEGORY).getName());

//                btnBottomCategoryNext.setEnabled(false);
//                btnBottomCategoryNext.setVisibility(View.INVISIBLE);

                nextCategoryName.setText("To First");

                btnBottomCategoryPrevious.setVisibility(View.VISIBLE);
                btnBottomCategoryPrevious.setEnabled(true);
            }


        } else if (categoryIndex == 0 && data.size() > 1) {

//            btnTopCategoryPrevious.setEnabled(false);
//            btnTopCategoryPrevious.setVisibility(View.INVISIBLE);

            btnTopCategoryNext.setEnabled(true);
            btnTopCategoryNext.setVisibility(View.VISIBLE);

            if (isTablet()) {
                nextCategoryName.setText(data.get(categoryIndex + NEXT_CATEGORY).getName());
                btnBottomCategoryNext.setOnClickListener(this);


//                btnBottomCategoryPrevious.setVisibility(View.INVISIBLE);
//                btnBottomCategoryPrevious.setEnabled(false);

                prevCategoryName.setText("To Last");

                btnBottomCategoryNext.setEnabled(true);
                btnBottomCategoryNext.setVisibility(View.VISIBLE);
            }


        } else {

            btnTopCategoryPrevious.setVisibility(View.VISIBLE);
            btnTopCategoryPrevious.setEnabled(true);

            if (isTablet()) {
                nextCategoryName.setText(data.get(categoryIndex + NEXT_CATEGORY).getName());
                prevCategoryName.setText(data.get(categoryIndex + PREV_CATEGORY).getName());
                btnBottomCategoryNext.setVisibility(View.VISIBLE);
                btnBottomCategoryNext.setEnabled(true);
                btnBottomCategoryPrevious.setVisibility(View.VISIBLE);
                btnTopCategoryPrevious.setEnabled(true);
            }

        }

        productAdapter.notifyDataSetChanged();

    }


    @Override
    protected void initViews() {

        nestedScrollView = findViewById(R.id.scroll_container);

        btnTopCategoryPrevious = findViewById(R.id.category_arrow_left);
        btnTopCategoryNext = findViewById(R.id.category_arrow_right);
        txtCategoryName = findViewById(R.id.category_title);
        productsRecycler = findViewById(R.id.product_list);

        buttonFind = findViewById(R.id.button_find);
        findEditText = findViewById(R.id.search_edit_text);

        // For tablet
        btnBottomCategoryPrevious = findViewById(R.id.previous_category_button);
        btnBottomCategoryNext = findViewById(R.id.next_category_button);
        prevCategoryName = findViewById(R.id.previous_category_title);
        nextCategoryName = findViewById(R.id.next_category_title);
        toolbarRestaurantTitle = findViewById(R.id.toolbar_restaurant_title);
        toolbarBack = findViewById(R.id.back_to_category);

        if (isTablet())
            toolbarBack.setOnClickListener(view -> finish());


        productAdapter = new ProductAdapter();
        productsRecycler.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.product_span_count)));
        productsRecycler.setAdapter(productAdapter);


        buttonFind.setOnClickListener(view -> {
            if (!findEditText.getText().toString().equals("")) {
                productAdapter.findProductBy(findEditText.getText().toString());
                findEditText.setText("");
            }
        });


        if (isTablet()) {
            btnBottomCategoryNext.setOnClickListener(this);
            btnBottomCategoryPrevious.setOnClickListener(this);
        }

        btnTopCategoryNext.setOnClickListener(this);
        btnTopCategoryPrevious.setOnClickListener(this);

    }


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_category;
    }

    @Override
    protected void attachPresenter() {
        Logger.log("Attach");
        if (presenter == null) {
            presenter = new CategoryPresenter(restaurantId, serviceId);
        }

        presenter.attachView(this);
        presenter.loadData(categoryId);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.category_arrow_left:
            case R.id.previous_category_button: {

                if (categoryIndex == 0)
                    categoryIndex = categories.size() - 1;
                else
                    categoryIndex += PREV_CATEGORY;

                categoryId = categories.get(categoryIndex).geId();
                changeCategoryTo(categoryId);
                break;
            }
            case R.id.category_arrow_right:
            case R.id.next_category_button: {

                if (categoryIndex + 1 == categories.size())
                    categoryIndex = 0;
                else
                    categoryIndex += NEXT_CATEGORY;

                categoryId = categories.get(categoryIndex).geId();
                changeCategoryTo(categoryId);
                break;
            }

        }
    }


    @Override
    public void showEmptyView() {

    }

    @Override
    public void hideEmptyView() {

    }

    @Override
    public void showError() {
        Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private int getCategoryIndex(List<Category> categories, int categoryId) {

        for (int i = 0; i < categories.size(); i++) {
            if (categoryId == categories.get(i).geId())
                return i;
        }

        return -1;
    }

}
