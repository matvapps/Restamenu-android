package com.restamenu.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.model.content.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr
 */

public class LanguageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Language> items;

    public LanguageAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<Language> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<Language> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public Language getItem(int position) {
        return items.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_language, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        LanguageViewHolder languageViewHolder = (LanguageViewHolder) holder;

        languageViewHolder.name.setText(getItem(position).getName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class LanguageViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView image;

        public LanguageViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.language_name);
//            image = itemView.findViewById(R.id.language_image);

        }
    }

}
