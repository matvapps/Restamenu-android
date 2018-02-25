package com.restamenu.category;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.restamenu.R;
import com.restamenu.api.RepositoryProvider;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Language;
import com.restamenu.model.content.Product;
import com.restamenu.rx.BaseSchedulerProvider;
import com.restamenu.rx.SchedulerProvider;
import com.restamenu.util.Logger;

import java.util.List;

import io.reactivex.Flowable;

public class CategoryFragment extends Fragment implements OnCategoryDataChangeListener {

    private static final String KEY_CATEGORY = "key_category";
    private static final String KEY_RESTAURANT = "key_restaurant";
    private static final String KEY_SERVICE = "key_service";
    private BaseSchedulerProvider schedulerProvider;
    //private SwipeRefreshLayout refreshLayout;
    private RecyclerView recycler;
    private ProductAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Category category;
    private int restaurantId;
    private int serviceId;

    private boolean isVisible = false;

    @NonNull
    public static CategoryFragment create(@NonNull Category category, @NonNull Integer restaurantId,
                                          @NonNull Integer serviceId) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_CATEGORY, category);
        args.putInt(KEY_RESTAURANT, restaurantId);
        args.putInt(KEY_SERVICE, serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        category = getArguments().getParcelable(KEY_CATEGORY);
        restaurantId = getArguments().getInt(KEY_RESTAURANT);
        serviceId = getArguments().getInt(KEY_SERVICE);
        super.onCreate(savedInstanceState);

//        ((CategoryActivity) getActivity()).registerOnCategoryDataChangeListener(this);

        schedulerProvider = SchedulerProvider.getInstance();
        adapter = new ProductAdapter();

        String currencyIcon = ((CategoryActivity) getActivity()).getSettingView().getCurrentCurrency().getSymbol();
        adapter.setCurrencyIcon(currencyIcon);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ((CategoryActivity) getActivity()).unregisterOnCategoryDataChangeListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.recycler);
        //recycler.setNestedScrollingEnabled(false);
//        recycler.setHasFixedSize(true);

        recycler.setClipToPadding(false);

        layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.product_span_count));

        recycler.setLayoutManager(layoutManager);

//        recycler.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        recycler.setAdapter(adapter);

        if (category != null) {
            loadProducts(restaurantId, category.geId());
        }
    }

    public void loadProducts(int restaurantId, int categoryId) {
        //showLoading(true);
        //refreshLayout.setRefreshing(true);
        RepositoryProvider.getAppRepository().getCategoryProducts(restaurantId, categoryId)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(products -> {
                    Logger.log("Products: " + products.toString());
                    //refreshLayout.setRefreshing(false);
                    //showLoading(false);
                    setProducts(products);
                }, throwable -> showError());
    }

    public void setProducts(List<Product> products) {
        Logger.log("Category set products : " + products.size());
        adapter.setItems(products);
    }

    private void showError() {
        Toast.makeText(getContext(), "Error Products", Toast.LENGTH_SHORT).show();

    }

    private void showLoading(boolean visible) {
        ((CategoryActivity) getActivity()).showLoading(visible);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        isVisible = isVisibleToUser;
    }

    protected boolean isTablet() {
        return getResources().getBoolean(R.bool.isLargeLayout);
    }


    @Override
    public void onPerformSearch(String searchString, int position) {
        if (isVisible)
            adapter.findProductBy(searchString);
    }

    @Override
    public void onLangChange(Language language, int position) {

    }

    @Override
    public void onCurrChange(Currency currency, int position) {

    }
}
