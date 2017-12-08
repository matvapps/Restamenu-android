package com.restamenu.model.content;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.restamenu.R;

import java.util.List;

/**
 * Created by Alexandr.
 */

public class Promotion extends AbstractItem<Promotion, Promotion.ViewHolder> {

    String background;


    public Promotion() {}

    @Override
    public Promotion.ViewHolder getViewHolder(@NonNull View v) {
        return new Promotion.ViewHolder(v);
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.restaurant_promotion_item;
    }

    @Override
    public void bindView(@NonNull ViewHolder holder, @NonNull List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.backgroundView.setImageResource(R.drawable.restaurants);

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView backgroundView;

        public ViewHolder(View view) {
            super(view);

            backgroundView = view.findViewById(R.id.promotion_background);

        }
    }

}
