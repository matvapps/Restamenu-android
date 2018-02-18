package com.restamenu.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.model.content.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Image> items;
    private boolean useScrollIt;

    public GalleryAdapter() {
        items = new ArrayList<>();
        useScrollIt = false;
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView;

        if (viewType == 0 && useScrollIt)
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scroll_it_item, parent, false);
        else
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.restaurant_gallery_item, parent, false);

        return new ImageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        if (!useScrollIt) {

            String image = items.get(position).getImage();
            String backgroundUrl = image.substring(1, image.length());
            Glide.with(holder.itemView.getContext())
                    .load(BuildConfig.BASE_URL + backgroundUrl + BuildConfig.IMAGE_WIDTH_400)
                    .apply(new RequestOptions()
                            .placeholder(R.color.greyish)
                            .override(400, 400)
                            .fitCenter())
                    .into(imageViewHolder.photo);


        } else if (position > 0 && useScrollIt) {
            String image = items.get(position).getImage();
            String backgroundUrl = image.substring(1, image.length());
            Glide.with(holder.itemView.getContext())
                    .load(BuildConfig.BASE_URL + backgroundUrl + BuildConfig.IMAGE_WIDTH_400)
                    .apply(new RequestOptions()
                            .placeholder(R.color.greyish)
                            .override(400, 400)
                            .fitCenter())
                    .into(imageViewHolder.photo);
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

    public boolean isUseScrollIt() {
        return useScrollIt;
    }

    public void setUseScrollIt(boolean useScrollIt) {
        this.useScrollIt = useScrollIt;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;

        public ImageViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.gallery_photo);
        }
    }

}