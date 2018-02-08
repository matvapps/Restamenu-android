package com.restamenu.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.base.BasePresenterActivity;
import com.restamenu.data.preferences.KeyValueStorage;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Language;
import com.restamenu.util.Logger;
import com.restamenu.views.custom.CustomSwipeToRefresh;
import com.restamenu.views.pager.CircularViewPagerHandler;
import com.restamenu.views.pager.MaterialViewPager;
import com.restamenu.views.setting.OnSettingItemChanged;
import com.restamenu.views.setting.SettingView;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends BasePresenterActivity<CategoryPresenter, CategoryView, List<Category>>
        implements CategoryView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OnSettingItemChanged {

    public static final String KEY_RESTAURANT = "key_rest";
    public static final String KEY_SERVICE_ID = "key_service_id";
    public static final String KEY_CATEGORY_ID = "key_category_id";
    public static final String KEY_RESTAURANT_TITLE = "key_name_id";
    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";
    public static final String KEY_RESTAURANT_LANGS = "key_restaurant_langs";
    public static final String KEY_RESTAURANT_CURRS = "key_restaurant_currs";

    private final int NEXT_CATEGORY = 1;
    private final int PREV_CATEGORY = -1;

    private Integer serviceId;
    private Integer categoryId;
    private int restaurantId;
    private String restaurantTitle;
    private int[] restCurrencies;
    private int[] restLanguages;

    private int categoryIndex = 0;

    private KeyValueStorage keyValueStorage;

    private SettingView settingView;
    private CustomSwipeToRefresh swipeRefreshLayout;
    private List<OnCategoryDataChangeListener> onCategoryDataChangeListeners;

    //private View productHeader;
    //Header
    private ImageView btnTopCategoryPrevious;
    private ImageView btnTopCategoryNext;
    private TextView txtCategoryName;

    private EditText findEditText;
    private View buttonFind;

    //Tablet
    private View btnBottomCategoryPrevious;
    private View btnBottomCategoryNext;
    private TextView prevCategoryName;
    private TextView nextCategoryName;

//    private Restaurant restaurant;

    private MaterialViewPager viewPager;
    private CategoryPagerAdapter pagerAdapter;

    private List<Category> categories;

    public static void start(@NonNull Context context, @NonNull int restaurantId, @NonNull String restaurantTitle,
                             @NonNull Integer serviceId, @NonNull Integer categoryId,
                             @NonNull int[] currencies, @NonNull int[] languages) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(KEY_SERVICE_ID, serviceId);
//        intent.putExtra(KEY_RESTAURANT, restaurant);
        intent.putExtra(KEY_RESTAURANT_ID, restaurantId);
        intent.putExtra(KEY_RESTAURANT_TITLE, restaurantTitle);
        intent.putExtra(KEY_RESTAURANT_LANGS, languages);
        intent.putExtra(KEY_RESTAURANT_CURRS, currencies);
        intent.putExtra(KEY_CATEGORY_ID, categoryId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        restaurant = (Restaurant) getIntent().getSerializableExtra(KEY_RESTAURANT);
        serviceId = getIntent().getIntExtra(KEY_SERVICE_ID, 1);
        categoryId = getIntent().getIntExtra(KEY_CATEGORY_ID, 1);
        restaurantTitle = getIntent().getStringExtra(KEY_RESTAURANT_TITLE);
        restCurrencies = getIntent().getIntArrayExtra(KEY_RESTAURANT_CURRS);
        restLanguages = getIntent().getIntArrayExtra(KEY_RESTAURANT_LANGS);
        restaurantId = getIntent().getIntExtra(KEY_RESTAURANT_ID, 1);

        onCategoryDataChangeListeners = new ArrayList<>();


        super.onCreate(savedInstanceState);
        keyValueStorage = new KeyValueStorage(this);

        toolbarTitle.setText(restaurantTitle);
    }

    @Override
    protected boolean showToolbarBackStack() {
        return true;
    }

    private void increaseCategory() {
        int position = viewPager.getViewPager().getCurrentItem();
        viewPager.getViewPager().setCurrentItem(position + 1, true);
    }

    private void decreaseCategory() {
        int position = viewPager.getViewPager().getCurrentItem();
        viewPager.getViewPager().setCurrentItem(position - 1, true);
    }

    private void updateCategory() {
        int position = viewPager.getViewPager().getCurrentItem();
        viewPager.getViewPager().setCurrentItem(position, true);
    }

    @Override
    public void setData(@NonNull List<Category> data) {
        Logger.log("Set category(ies): " + data.size());
        categories = data;

        pagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), data, restaurantId, serviceId);
        viewPager.getViewPager().setAdapter(pagerAdapter);

        final CircularViewPagerHandler circularViewPagerHandler = new CircularViewPagerHandler(viewPager.getViewPager());
        circularViewPagerHandler.setOnPageChangeListener(createOnPageChangeListener());
        viewPager.getViewPager().addOnPageChangeListener(circularViewPagerHandler);

        categoryIndex = getCategoryIndex(data, categoryId);
        Logger.log("categoryIndex = " + categoryIndex);

        txtCategoryName.setText(data.get(categoryIndex).getName());
        viewPager.getViewPager().setCurrentItem(categoryIndex + 1, false);

        //pager.scrollToPosition(categoryIndex);
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
        viewPager = findViewById(R.id.materialViewPager);

        btnTopCategoryPrevious = findViewById(R.id.category_arrow_left);
        btnTopCategoryNext = findViewById(R.id.category_arrow_right);
        txtCategoryName = findViewById(R.id.category_title);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        View back = findViewById(R.id.back_to_category);
        settingView = findViewById(R.id.settings_view);

        swipeRefreshLayout.setOnRefreshListener(this);
        settingView.setOnSettingItemChanged(this);

        //productHeader = findViewById(R.id.product_header);
        buttonFind = findViewById(R.id.button_find);
        findEditText = findViewById(R.id.search_edit_text);
        findEditText.clearFocus();


        buttonFind.setOnClickListener(view -> onPerformSearch(findEditText.getText().toString()));

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
            case R.id.category_arrow_left:
            case R.id.previous_category_button: {
                decreaseCategory();
                break;
            }
            case R.id.category_arrow_right:
            case R.id.next_category_button: {
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

    private int getCategoryIndex(List<Category> categories, int categoryId) {

        for (int i = 0; i < categories.size(); i++) {
            if (categoryId == categories.get(i).geId())
                return i;
        }
        return -1;
    }

    @Override
    public void setLanguages(@NonNull List<Language> languages) {
        List<Language> restLangList = new ArrayList<>();

        for (int langId : restLanguages) {
            for (int i = 0; i < languages.size(); i++) {
                if (languages.get(i).getLanguage_id() == langId) {
                    restLangList.add(languages.get(i));
                }
            }
        }
        settingView.setLanguages(restLangList);
    }

    @Override
    public void setCurrencies(@NonNull List<Currency> currencies) {
        List<Currency> restCurrList = new ArrayList<>();

        for (int currId : restCurrencies) {
            for (int i = 0; i < currencies.size(); i++) {
                if (currencies.get(i).getCurrency_id() == currId) {
                    restCurrList.add(currencies.get(i));
                }
            }
        }
        settingView.setCurrencies(restCurrList);
    }

    private ViewPager.OnPageChangeListener createOnPageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                Logger.log("Position selected: " + position);
                try {
                    txtCategoryName.setText(categories.get(position).getName());
                    categoryIndex = position;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
            }
        };
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

    @Override
    public void onRefresh() {
        presenter.loadData(keyValueStorage.getLanguageId());
        categoryId = categories.get(viewPager.getViewPager().getCurrentItem() - 1).geId();
        swipeRefreshLayout.setRefreshing(false);
    }

    public synchronized void registerOnCategoryDataChangeListener (OnCategoryDataChangeListener onCategoryDataChangeListener) {
        onCategoryDataChangeListeners.add(onCategoryDataChangeListener);
    }

    public synchronized void unregisterOnCategoryDataChangeListener(OnCategoryDataChangeListener onCategoryDataChangeListener) {
        onCategoryDataChangeListeners.remove(onCategoryDataChangeListener);
    }


    public synchronized void langChanged(Language language) {
        for (OnCategoryDataChangeListener item: onCategoryDataChangeListeners)
            item.onLangChange(language, viewPager.getViewPager().getCurrentItem());
    }

    public synchronized void currChanged(Currency currency) {
        for (OnCategoryDataChangeListener item: onCategoryDataChangeListeners)
            item.onCurrChange(currency, viewPager.getViewPager().getCurrentItem());
    }

    public synchronized void onPerformSearch(String str) {
        for (OnCategoryDataChangeListener item: onCategoryDataChangeListeners)
            item.onPerformSearch(str, viewPager.getViewPager().getCurrentItem());
    }

    /**
     * OnSettingItemChanged listeners
     */
    @Override
    public void onLanguageChanged(Language language) {
//        onCategoryDataChangeListener.onLangChange(language);
    }

    @Override
    public void onCurrencyChanged(Currency currency) {
//        onCategoryDataChangeListener.onCurrChange(currency);
    }
    /**------------------------------------------- */
}
