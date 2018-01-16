package com.restamenu.category;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.restamenu.model.content.Category;
import com.restamenu.views.pager.BaseCircularViewPagerAdapter;

import java.util.List;

/**
 * Created by alessio on 05.01.2018.
 */

public class CategoryPagerAdapter extends BaseCircularViewPagerAdapter<Category> {

    private int restaurantId;
    private int serviceId;

    public CategoryPagerAdapter(FragmentManager fragmentManager, List<Category> categories, int restaurantId, int serviceId) {
        super(fragmentManager, categories);
        this.restaurantId = restaurantId;
        this.serviceId = serviceId;
    }

    @Override
    protected Fragment getFragmentForItem(Category category) {
        return CategoryFragment.create(category, restaurantId, serviceId);
    }
}
