package com.restamenu.restaurant.adapter;

import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.restamenu.R;
import com.restamenu.model.content.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class OrderTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ListAdapter{

    private List<Image> items;

    public OrderTypeAdapter() {
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

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;

        public ImageViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.gallery_photo);
        }
    }

}