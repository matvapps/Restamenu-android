package com.restamenu.base;

/**
 * @author Roodie
 */

public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}
