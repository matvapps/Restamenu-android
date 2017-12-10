package com.restamenu.category;

import com.restamenu.base.Presenter;

/**
 * Created by Alexandr.
 */

public class CategoryPresenter implements Presenter<CategoryView> {

    private CategoryView view;

    @Override
    public void attachView(CategoryView view) {
        this.view = view;

    }

    @Override
    public void detachView() {
        this.view = null;

    }
}
