package com.restamenu.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedImageView;
import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.model.content.Promotion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class PromotionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Promotion> items;

    public PromotionsAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<Promotion> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<Promotion> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_promotion_item, parent, false);

        return new PromotionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PromotionViewHolder promotionViewHolder = (PromotionViewHolder) holder;

        String image = items.get(position).getImage();
        String backgroundUrl = image.substring(1, image.length());
        Picasso.with(holder.itemView.getContext())
                .load(BuildConfig.BASE_URL + backgroundUrl).into(promotionViewHolder.background);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class PromotionViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView background;

        public PromotionViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.promotion_background);
        }
    }

}
