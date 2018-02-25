package com.restamenu.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.restamenu.R;
import com.restamenu.base.BasePresenterActivity;
import com.restamenu.data.preferences.KeyValueStorage;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Language;
import com.restamenu.model.content.Product;
import com.restamenu.restaurant.adapter.AdapterItemType;
import com.restamenu.restaurant.adapter.ItemType;
import com.restamenu.util.Logger;
import com.restamenu.views.custom.CustomSwipeToRefresh;
import com.restamenu.views.custom.OnVerticalScrollListener;
import com.restamenu.views.setting.OnSettingItemChanged;
import com.restamenu.views.setting.SettingListener;
import com.restamenu.views.setting.SettingView;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends BasePresenterActivity<CategoryPresenter, CategoryView, List<Category>>
        implements CategoryView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OnSettingItemChanged,
        SettingListener, CategoryAdapter.OnNavigationButtonClickListener {

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
    private Language currentLanguage;
    private Currency currentCurrency;
    private RecyclerView categoryListContainer;



    private List<Category> categories;
    private CategoryAdapter categoryAdapter;

    public static void start(@NonNull Context context, @NonNull int restaurantId, @NonNull String restaurantTitle,
                             @NonNull Integer serviceId, @NonNull Integer categoryId,
                             @NonNull int[] currencies, @NonNull int[] languages) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(KEY_SERVICE_ID, serviceId);
        intent.putExtra(KEY_RESTAURANT_ID, restaurantId);
        intent.putExtra(KEY_RESTAURANT_TITLE, restaurantTitle);
        intent.putExtra(KEY_RESTAURANT_LANGS, languages);
        intent.putExtra(KEY_RESTAURANT_CURRS, currencies);
        intent.putExtra(KEY_CATEGORY_ID, categoryId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serviceId = getIntent().getIntExtra(KEY_SERVICE_ID, 1);
        categoryId = getIntent().getIntExtra(KEY_CATEGORY_ID, 1);
        restaurantTitle = getIntent().getStringExtra(KEY_RESTAURANT_TITLE);
        restCurrencies = getIntent().getIntArrayExtra(KEY_RESTAURANT_CURRS);
        restLanguages = getIntent().getIntArrayExtra(KEY_RESTAURANT_LANGS);
        restaurantId = getIntent().getIntExtra(KEY_RESTAURANT_ID, 1);

        currentCurrency = new Currency();
        currentLanguage = new Language();

        super.onCreate(savedInstanceState);
        keyValueStorage = new KeyValueStorage(this);

        toolbarTitle.setText(restaurantTitle);
    }

    @Override
    protected boolean showToolbarBackStack() {
        return true;
    }

    private void increaseCategory() {
        presenter.loadProducts(restaurantId, getNextCategory().geId());
        categoryAdapter.setCategoryIndex(categoryIndex);
    }

    private void decreaseCategory() {
        presenter.loadProducts(restaurantId, getPreviousCategory().geId());
        categoryAdapter.setCategoryIndex(categoryIndex);
    }


    @Override
    public void setProducts(List<Product> products) {
        categoryAdapter.change(new AdapterItemType<>("", categories.get(categoryIndex), ItemType.HEADER), 0);
        if (isTablet()) {
            categoryAdapter.change(new AdapterItemType<>("", products, ItemType.PRODUCT_MENU), 1);
            categoryAdapter.change(new AdapterItemType<>("", categories.get(categoryIndex), ItemType.FOOTER), 2);
        } else {
            categoryAdapter.change(new AdapterItemType<>("", products, ItemType.PRODUCT_MENU_PHONE), 1);
        }
    }

    @Override
    public void setData(@NonNull List<Category> data) {
        Logger.log("Set category(ies): " + data.size());
        categories = data;

        categoryIndex = getCategoryIndex(data, categoryId);

        categoryAdapter.setCategories(categories);
        categoryAdapter.setCategoryIndex(categoryIndex);
        presenter.loadProducts(restaurantId, categoryId);
    }

    @Override
    protected void initViews() {

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        settingView = findViewById(R.id.settings_view);
        categoryListContainer = findViewById(R.id.category_list_container);

        categoryListContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoryListContainer.setNestedScrollingEnabled(false);
        categoryListContainer.addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrolledDown(int dy) {
                super.onScrolledDown(dy);
                onSwipeRefreshEnabled(false);
            }

            @Override
            public void onScrolledToTop() {
                super.onScrolledToTop();
                onSwipeRefreshEnabled(true);
            }
        });


        categoryAdapter = new CategoryAdapter(this);

//        categoryAdapter.setCurrentLanguage(currentLanguage);
//        categoryAdapter.setCurrentCurrency(currentCurrency);

        int quantity = 2;
        if (isTablet())
            quantity = 3;


        for (int i = 0; i < quantity; i++) {
            categoryAdapter.add(new AdapterItemType<>("", null, ItemType.PRODUCT_MENU));
        }
        categoryListContainer.setAdapter(categoryAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        settingView.setOnSettingItemChanged(this);
        settingView.setSettingListener(this);

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

    }

    public Category getNextCategory() {
        categoryIndex = (categoryIndex + 1) % categories.size();

        return categories.get(categoryIndex);
    }

    public Category getPreviousCategory() {
        categoryIndex = (categoryIndex - 1) % categories.size();
        if (categoryIndex < 0)
            categoryIndex = categories.size() - 1;

        return categories.get(categoryIndex);
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

        for (Language language : languages)
            if (language.getLanguage_id() == keyValueStorage.getLanguageId())
                currentLanguage = language;

        categoryAdapter.setCurrentLanguage(currentLanguage);

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

        for (Currency currency : currencies)
            if (currency.getCurrency_id() == keyValueStorage.getCurrencyId())
                currentCurrency = currency;

        categoryAdapter.setCurrentCurrency(currentCurrency);

        settingView.setCurrencies(restCurrList);
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
//        categoryId = categories.get(viewPager.getCurrentItem() - 1).geId();
        swipeRefreshLayout.setRefreshing(false);
//        findEditText.setText(null);
    }

    /**
     * OnSettingItemChanged listeners
     */
    @Override
    public void onLanguageChanged(Language language) {
        Logger.log("");
        currentLanguage = language;
        categoryAdapter.setCurrentLanguage(language);
        presenter.loadData(language.getLanguage_id());
//        categoryId = categories.get(viewPager.getCurrentItem() - 1).geId();
    }

    @Override
    public void onCurrencyChanged(Currency currency) {
        Logger.log("");
        currentCurrency = currency;
        categoryAdapter.setCurrentCurrency(currency);
        presenter.loadData(keyValueStorage.getLanguageId());
//        categoryId = categories.get(viewPager.getCurrentItem() - 1).geId();
    }

    @Override
    public void popupOpened() {
        showTransparentView(true);
    }

    @Override
    public void popupClosed() {
        showTransparentView(false);
    }

    /**
     * -------------------------------------------
     */

//    public Language getCurrentLanguage() {
//        return currentLanguage;
//    }
//
//    public void setCurrentLanguage(Language currentLanguage) {
//        this.currentLanguage = currentLanguage;
//    }
//
//    public Currency getCurrentCurrency() {
//        return currentCurrency;
//    }
//
//    public void setCurrentCurrency(Currency currentCurrency) {
//        this.currentCurrency = currentCurrency;
//    }




    public SettingView getSettingView() {
        return settingView;
    }



    /**
     * Actions Listeners
     */
    @Override
    public void onNextCategory() {
        increaseCategory();
    }

    @Override
    public void onPreviousCategory() {
        decreaseCategory();
    }

    @Override
    public void onPerformSearch(String keyword, List<Product> products) {

        ProductAdapter productAdapter = new ProductAdapter();
        productAdapter.setItems(products);
        productAdapter.findProductBy(keyword);

        if (isTablet()) {
            categoryAdapter.change(new AdapterItemType<>("", productAdapter.getItems(), ItemType.PRODUCT_MENU), 1);
        } else {
            categoryAdapter.change(new AdapterItemType<>("", productAdapter.getItems(), ItemType.PRODUCT_MENU_PHONE), 1);
        }
    }

    @Override
    public void onSwipeRefreshEnabled(boolean enabled) {
        swipeRefreshLayout.setEnabled(enabled);
    }
    /**-------------------------------------*/


}
