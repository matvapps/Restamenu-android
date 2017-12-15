package com.restamenu;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                .inflate(R.layout.popup_item, parent, false);

        return new PopupViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PopupViewHolder popupViewHolder = (PopupViewHolder) holder;

        if(items.get(position).isChecked())
            popupViewHolder.chbx.setVisibility(View.VISIBLE);
        else
            popupViewHolder.chbx.setVisibility(View.INVISIBLE);

        popupViewHolder.itemView.setOnClickListener(view -> {

            Logger.log(String.valueOf(items.get(position).isChecked()));

            if (items.get(position).isChecked()) {
                items.get(position).setChecked(false);
                popupViewHolder.chbx.setVisibility(View.INVISIBLE);
            } else {
                items.get(position).setChecked(true);
                popupViewHolder.chbx.setVisibility(View.VISIBLE);
            }

            if (popupItemClickListener != null) {
                popupItemClickListener.onClick(items.get(position));

                Logger.log(String.valueOf(items.get(position).isChecked()));

            }
        });

        if (items.get(position).getItem() instanceof Cusine) {
            popupViewHolder.title.setText(((Cusine) items.get(position).getItem()).getName());

        } else if (items.get(position).getItem() instanceof Institute) {
            popupViewHolder.title.setText(((Institute) items.get(position).getItem()).getName());
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

    class PopupViewHolder extends RecyclerView.ViewHolder {
        private View chbx;
        private TextView title;

        public PopupViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.popup_item_title);
            chbx = itemView.findViewById(R.id.popup_item_chbx_checked);

        }
    }

}
