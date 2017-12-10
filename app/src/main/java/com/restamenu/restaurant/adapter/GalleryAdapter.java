package com.restamenu.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.restamenu.R;
import com.restamenu.model.content.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Image> items;

    public GalleryAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<Image> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<Image> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_gallery_item, parent, false);

        return new ImageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ImageViewHolder promotionViewHolder = (ImageViewHolder) holder;

        promotionViewHolder.photo.setImageResource(R.drawable.restaurants);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;

        public ImageViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.gallery_photo);
        }
    }

}