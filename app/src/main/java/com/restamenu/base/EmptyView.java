package com.restamenu.base;

/**
 * @author Roodie
 */

public interface EmptyView<D> extends BaseView<D> {

    void showEmptyView();

    void hideEmptyView();
}
