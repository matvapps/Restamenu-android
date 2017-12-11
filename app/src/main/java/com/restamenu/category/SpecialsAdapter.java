package com.restamenu.category;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class SpecialsAdapter extends RecyclerView.Adapter<SpecialsAdapter.SpecialViewHolder> {

    private List<String> items;

    SpecialsAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<String> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<String> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public SpecialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if (items.size() < 2)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.special_item_layout_big, parent, false);
        else
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.special_item_layout, parent, false);

        return new SpecialViewHolder(view);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(SpecialViewHolder holder, int position) {

        if (items.size() < 2)
            holder.name.setText(items.get(position));



    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class SpecialViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView name;

        public SpecialViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.specials_icon);
            name = itemView.findViewById(R.id.specials_name);

        }


    }

}
