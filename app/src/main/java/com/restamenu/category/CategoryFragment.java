package com.restamenu.category;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.restamenu.R;
import com.restamenu.api.RepositoryProvider;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Product;
import com.restamenu.rx.BaseSchedulerProvider;
import com.restamenu.rx.SchedulerProvider;
import com.restamenu.util.Logger;

import java.util.List;

import io.reactivex.Flowable;


public class CategoryFragment extends Fragment {

    private BaseSchedulerProvider schedulerProvider;

    private RecyclerView recycler;
    private ProductAdapter adapter;

    private Category category;
    private int restaurantId;
    private int serviceId;


    private static final String KEY_CATEGORY = "key_category";
    private static final String KEY_RESTAURANT = "key_restaurant";
    private static final String KEY_SERVICE = "key_service";

    @NonNull
    public static CategoryFragment create(@NonNull Category category, @NonNull Integer restaurantId, @NonNull Integer serviceId) {
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

        schedulerProvider = SchedulerProvider.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_items, container, false);
        recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.product_span_count)));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ProductAdapter();
        recycler.setAdapter(adapter);

        if (category != null) {
            loadProducts(restaurantId, serviceId, category.geId());
        }
    }

    public void loadProducts(int restaurantId, int serviceId, int categoryId) {
        showLoading(true);
        RepositoryProvider.getAppRepository().getCategoryProducts(restaurantId, categoryId)
                .flatMap(Flowable::fromIterable)
                .toList()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(products -> {
                    Logger.log("Products: " + products.toString());
                    showLoading(false);
                    setProducts(products);
                }, throwable -> showError());
    }

    public void setProducts(List<Product> products) {
        Logger.log("Products : " + products.toString());
        adapter.setItems(products);
    }

    private void showError() {
        ((CategoryActivity) getActivity()).showError();

    }

    private void showLoading(boolean visible) {
        ((CategoryActivity) getActivity()).showLoading(visible);
    }

}
