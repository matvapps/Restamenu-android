package com.restamenu.base;

import android.support.annotation.NonNull;

/**
 * @author Roodie
 */

public interface BaseView<T> {

    void setData(@NonNull T data);

    void showError();

    void showLoading(boolean show);
}
