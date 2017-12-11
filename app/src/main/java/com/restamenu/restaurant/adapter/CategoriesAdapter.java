package com.restamenu.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.category.CategoryActivity;
import com.restamenu.model.content.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> items;
    private CategoryClickListener onCategoryClickListener;
    private int restId = 1;

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

        // TODO: load image
        categoryViewHolder.background.setImageResource(R.drawable.restaurants);
        categoryViewHolder.name.setText(category.getName());

        categoryViewHolder.itemView.setOnClickListener(view -> {

            CategoryActivity.start(context, restId,1,  items.get(position).geId());

//                if (onCategoryClickListener != null)
//                    onCategoryClickListener.onCategoryClicked(categoryViewHolder.getAdapterPosition());
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
