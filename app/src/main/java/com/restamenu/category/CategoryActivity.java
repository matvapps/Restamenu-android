package com.restamenu.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsjwzh.widget.recyclerviewpager.FragmentStatePagerAdapter;
import com.lsjwzh.widget.recyclerviewpager.LoopRecyclerViewPager;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.restamenu.R;
import com.restamenu.base.BasePresenterActivity;
import com.restamenu.data.preferences.KeyValueStorage;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Language;
import com.restamenu.util.Logger;
import com.restamenu.views.custom.SettingView;

import java.util.LinkedHashMap;
import java.util.List;

import ru.noties.scrollable.CanScrollVerticallyDelegate;
import ru.noties.scrollable.OnFlingOverListener;
import ru.noties.scrollable.ScrollableLayout;

public class CategoryActivity extends BasePresenterActivity<CategoryPresenter, CategoryView, List<Category>> implements CategoryView, View.OnClickListener, RecyclerViewPager.OnPageChangedListener {

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


    private FrameLayout searchContainer;
    private LinearLayout searchButton;

    private KeyValueStorage keyValueStorage;

    private SettingView settingView;

    private View productHeader;
    private ImageView btnTopCategoryPrevious;
    private ImageView btnTopCategoryNext;
    private TextView txtCategoryName;
    private View btnBottomCategoryPrevious;
    private View btnBottomCategoryNext;
    private TextView prevCategoryName;
    private TextView nextCategoryName;

    private String restaurantTitle;

    //private TextView toolbarRestaurantTitle;
    private EditText findEditText;
    private View buttonFind;

    private ScrollableLayout scrollableLayout;

    private int selectedPage = 0;

    private FragmentsAdapter pagerAdapter;
    private LoopRecyclerViewPager pager;
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
        keyValueStorage = new KeyValueStorage(this);

        toolbarTitle.setText(restaurantTitle);
    }

    @Override
    protected boolean showToolbarBackStack() {
        return true;
    }

    private void increaseCategory() {
        pager.smoothScrollToPosition(pager.getActualCurrentPosition() - 1);
    }

    private void decreaseCategory() {
        pager.smoothScrollToPosition(pager.getActualCurrentPosition() + 1);
    }

    @Override
    public void setData(@NonNull List<Category> data) {
        Logger.log("Set category(ies): " + data.size());
        categories = data;

        pagerAdapter = new FragmentsAdapter(getSupportFragmentManager(), data, restaurantId, serviceId);
        pager.setAdapter(pagerAdapter);

        categoryIndex = getCategoryIndex(data, categoryId);

        Logger.log("Category index = " + categoryIndex);

        txtCategoryName.setText(data.get(categoryIndex).getName());

        pager.scrollToPosition(categoryIndex);
        btnTopCategoryPrevious.setOnClickListener(this);
        btnTopCategoryNext.setOnClickListener(this);

        if (isTablet()) {
            btnBottomCategoryPrevious.setOnClickListener(this);
            btnTopCategoryNext.setOnClickListener(this);
        }

        if (categoryIndex == (data.size() - 1)) {

            btnTopCategoryPrevious.setEnabled(true);
            btnTopCategoryPrevious.setVisibility(View.VISIBLE);


            if (isTablet()) {
                prevCategoryName.setText(data.get(categoryIndex + PREV_CATEGORY).getName());

                nextCategoryName.setText("To First");

                btnBottomCategoryPrevious.setVisibility(View.VISIBLE);
                btnBottomCategoryPrevious.setEnabled(true);
            }


        } else if (categoryIndex == 0 && data.size() > 1) {

            btnTopCategoryNext.setEnabled(true);
            btnTopCategoryNext.setVisibility(View.VISIBLE);

            if (isTablet()) {
                nextCategoryName.setText(data.get(categoryIndex + NEXT_CATEGORY).getName());
                btnBottomCategoryNext.setOnClickListener(this);

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
    }

    @Override
    protected void initViews() {
        scrollableLayout = findViewById(R.id.scrollable_layout);

        btnTopCategoryPrevious = findViewById(R.id.category_arrow_left);
        btnTopCategoryNext = findViewById(R.id.category_arrow_right);
        txtCategoryName = findViewById(R.id.category_title);
        pager = findViewById(R.id.viewpager);
        View back = findViewById(R.id.back_to_category);
        settingView = findViewById(R.id.settings_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        pager.setLayoutManager(layoutManager);
        pager.setHasFixedSize(true);
        pager.addOnPageChangedListener(this);

        scrollableLayout.setCanScrollVerticallyDelegate(new CanScrollVerticallyDelegate() {
            @Override
            public boolean canScrollVertically(int direction) {
                Logger.log("Can scroll vertically : dir: " + direction);
                if (direction > 0)
                    return true;
                else {
                    final CategoryFragment fragment = (CategoryFragment) pagerAdapter.getItem(selectedPage, null);
                    Logger.log("Can Current fragment scroll ? " + (fragment.canScrollVertically(direction)));
                    return fragment != null && fragment.canScrollVertically(direction);
                }
            }
        });

        scrollableLayout.setOnFlingOverListener(new OnFlingOverListener() {
            @Override
            public void onFlingOver(int y, long duration) {
                Logger.log("Can on fling : duration: " + duration);
                final CategoryFragment fragment = (CategoryFragment) pagerAdapter.getItem(selectedPage, null);
                if (fragment != null) {
                    fragment.onFlingOver(y, duration);
                }
            }
        });

        productHeader = findViewById(R.id.product_header);
        buttonFind = findViewById(R.id.button_find);
        findEditText = findViewById(R.id.search_edit_text);
        findEditText.clearFocus();

        // For tablet
        btnBottomCategoryPrevious = findViewById(R.id.previous_category_button);
        btnBottomCategoryNext = findViewById(R.id.next_category_button);
        prevCategoryName = findViewById(R.id.previous_category_title);
        nextCategoryName = findViewById(R.id.next_category_title);


        if (isTablet()) {
            btnBottomCategoryNext.setOnClickListener(this);
            btnBottomCategoryPrevious.setOnClickListener(this);
            back.setOnClickListener(view -> finish());
        }

        btnTopCategoryNext.setOnClickListener(this);
        btnTopCategoryPrevious.setOnClickListener(this);
        productHeader.setOnTouchListener(new OnSwipeTouchListener(CategoryActivity.this) {
            public void onSwipeRight() {
                increaseCategory();
            }

            public void onSwipeLeft() {
                decreaseCategory();
            }
        });
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_category;
    }

    @Override
    protected int getToolbarLayoutId() {
        return R.layout.toolbar_category;
    }

    @Override
    protected void attachPresenter() {
        Logger.log("Attach");
        if (presenter == null) {
            presenter = new CategoryPresenter(restaurantId, serviceId);
        }

        keyValueStorage = new KeyValueStorage(this);

        presenter.attachView(this);
        presenter.loadData(keyValueStorage.getLanguageId());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.category_arrow_right:
            case R.id.next_category_button: {
                decreaseCategory();
                break;
            }
            case R.id.category_arrow_left:
            case R.id.previous_category_button: {
                increaseCategory();
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private int getCategoryIndex(List<Category> categories, int categoryId) {

        for (int i = 0; i < categories.size(); i++) {
            if (categoryId == categories.get(i).geId())
                return i;
        }
        return -1;
    }

    @Override
    public void OnPageChanged(int i, int i1) {
        try {
            findEditText.setText("");
            txtCategoryName.setText(categories.get(pager.getActualCurrentPosition()).getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setLanguages(@NonNull List<Language> languages) {
        settingView.setLanguages(languages);
    }

    @Override
    public void setCurrencies(@NonNull List<Currency> currencies) {
        settingView.setCurrencies(currencies);
    }

    class FragmentsAdapter extends FragmentStatePagerAdapter {

        private int restaurantId;
        private int serviceId;
        private List<Category> categories;
        private LinkedHashMap<Integer, Fragment> mFragmentCache = new LinkedHashMap<>();


        public FragmentsAdapter(FragmentManager fm, List<Category> categories, int restaurantId, int serviceId) {
            super(fm);
            this.restaurantId = restaurantId;
            this.serviceId = serviceId;
            this.categories = categories;
        }

        private Category getCategory(int position) {
            return categories.get(position);
        }

        @Override
        public Fragment getItem(int position, Fragment.SavedState savedState) {
            position = pager.transformToActualPosition(position);
            Fragment f = mFragmentCache.containsKey(position) ? mFragmentCache.get(position)
                    : CategoryFragment.create(getCategory(position), restaurantId, serviceId);
            //Logger.log("Category getItem:" + position + " from cache: " + mFragmentCache.containsKey(position));
            if (!mFragmentCache.containsKey(position)) {
                f.setInitialSavedState(savedState);
                //Logger.log("Category setInitialSavedState:" + position);
            }
            mFragmentCache.put(position, f);
            return f;
        }

        @Override
        public void onDestroyItem(int position, Fragment fragment) {
            // onDestroyItem
            while (mFragmentCache.size() > 5) {
                Object[] keys = mFragmentCache.keySet().toArray();
                mFragmentCache.remove(keys[0]);
            }

        }

        @Override
        public int getItemCount() {
            return categories.size();
        }
    }

    @Override
    public void finish() {
        // dismiss setting popup window if it's showing on phone
        if (!isTablet()) {
            if (settingView.getSettingPopup().isShowing()) {
                settingView.dismissSettingPopup();
                return;
            }
        }

        super.finish();
    }

}
