package com.restamenu.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        View rootView;

        if (viewType == 0)
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scroll_it_item, parent, false);
        else
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.restaurant_gallery_item, parent, false);

        return new ImageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position != 0) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            imageViewHolder.photo.setImageResource(R.drawable.restaurants);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ScrollItViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView text;

        public ScrollItViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.scroll_it_image);
            text = itemView.findViewById(R.id.scroll_it_text);

        }

    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;

        public ImageViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.gallery_photo);
        }
    }

}