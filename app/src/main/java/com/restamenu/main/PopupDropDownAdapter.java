package com.restamenu.main;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Institute;
import com.restamenu.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class PopupDropDownAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PopupFilterItem> items;
    private PopupItemClickListener popupItemClickListener;

    public PopupDropDownAdapter() {
        items = new ArrayList<>();
    }


    public void addItems(List<PopupFilterItem> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<PopupFilterItem> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void addItem(PopupFilterItem popupFilterItem) {
        items.add(popupFilterItem);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_popup_item, parent, false);

        return new FilterViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        FilterViewHolder filterViewHolder = (FilterViewHolder) holder;

        if(items.get(position).isChecked())
            filterViewHolder.chbx.setVisibility(View.VISIBLE);
        else
            filterViewHolder.chbx.setVisibility(View.INVISIBLE);

        filterViewHolder.itemView.setOnClickListener(view -> {

            Logger.log(String.valueOf(items.get(position).isChecked()));

            if (items.get(position).isChecked()) {
                items.get(position).setChecked(false);
                filterViewHolder.chbx.setVisibility(View.INVISIBLE);
            } else {
                items.get(position).setChecked(true);
                filterViewHolder.chbx.setVisibility(View.VISIBLE);
            }

            if (popupItemClickListener != null) {
                popupItemClickListener.onClick(items.get(position));

                Logger.log(String.valueOf(items.get(position).isChecked()));

            }
        });

        if (items.get(position).getItem() instanceof Cusine) {
            filterViewHolder.title.setText(((Cusine) items.get(position).getItem()).getName());

        } else if (items.get(position).getItem() instanceof Institute) {
            filterViewHolder.title.setText(((Institute) items.get(position).getItem()).getName());
        }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public PopupItemClickListener getPopupItemClickListener() {
        return popupItemClickListener;
    }

    public void setPopupItemClickListener(PopupItemClickListener popupItemClickListener) {
        this.popupItemClickListener = popupItemClickListener;
    }


    class FilterViewHolder extends RecyclerView.ViewHolder {
        private View chbx;
        private TextView title;

        public FilterViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.popup_item_title);
            chbx = itemView.findViewById(R.id.popup_item_chbx_checked);

        }
    }

}
