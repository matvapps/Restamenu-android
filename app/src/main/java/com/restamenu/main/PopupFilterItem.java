package com.restamenu.main;

/**
 * Created by Alexandr.
 */

public class PopupFilterItem<T> {

    public PopupFilterItem(T item, boolean checked) {
        this.item = item;
        this.checked = checked;
    }

    T item;
    boolean checked;

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
