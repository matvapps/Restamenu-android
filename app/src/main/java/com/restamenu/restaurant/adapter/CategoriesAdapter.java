package com.restamenu.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.model.content.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> items;
    private CategoryClickListener onCategoryClickListener;
    private int restId;
    private String restaurantTitle;

    public String getRestaurantTitle() {
        return restaurantTitle;
    }

    public void setRestaurantTitle(String restaurantTitle) {
        this.restaurantTitle = restaurantTitle;
    }


    public CategoriesAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<Category> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<Category> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnCategoryClickListener(CategoryClickListener clickListener) {
        this.onCategoryClickListener = clickListener;
    }

    public void setRestaurantId(int restaurantId) {
        this.restId = restaurantId;
    }


    Context context;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_category_item, parent, false);

        context = parent.getContext();
        return new CategoryViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        Category category = items.get(position);

        if (items.get(position).getImage() != null && !items.get(position).getImage().equals("")) {

            String image = items.get(position).getImage();
            String backgroundUrl = image.substring(1, image.length());
            Picasso.with(holder.itemView.getContext())
                    .load(BuildConfig.BASE_URL + backgroundUrl).into(categoryViewHolder.background);
        }

        categoryViewHolder.name.setText(category.getName());

        categoryViewHolder.itemView.setOnClickListener(view -> {

//            CategoryActivity.start(context, restId,1,  items.get(position).geId(), restaurantTitle);

                if (onCategoryClickListener != null)
                    onCategoryClickListener.onCategoryClicked(items.get(position).geId());
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView background;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.category_name);
            background = itemView.findViewById(R.id.category_background);
        }
    }

}
