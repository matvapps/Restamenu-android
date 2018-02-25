package com.restamenu.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.model.content.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class CategoryTitleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> items;

    public CategoryTitleListAdapter() {
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_name, null);
        rootView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        return new TitleViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

        titleViewHolder.title.setText(items.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public TitleViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_title);
        }
    }

}
