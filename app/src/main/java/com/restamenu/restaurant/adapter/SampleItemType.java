package com.restamenu.restaurant.adapter;

import android.support.v7.widget.RecyclerView;

import com.mikepenz.fastadapter.items.AbstractItem;

/**
 * Created by Alexandr.
 */

/**
 * NOT USED
 */
public abstract class SampleItemType extends AbstractItem<SampleItemType, RecyclerView.ViewHolder> {

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

}
