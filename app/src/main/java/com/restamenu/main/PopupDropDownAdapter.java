package com.restamenu.main;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Institute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr.
 */

public class PopupDropDownAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CheckedItem> items;
//    private PopupItemClickListener popupItemClickListener;
    private FilterItemChangeListener filterItemChangeListener;

    public PopupDropDownAdapter() {
        items = new ArrayList<>();
    }


    public void setFilterItemChangeListener(FilterItemChangeListener filterItemChangeListener) {
        this.filterItemChangeListener = filterItemChangeListener;
    }

    public void addItems(List<CheckedItem> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<CheckedItem> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void addItem(CheckedItem checkedItem) {
        items.add(checkedItem);
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

            if (items.get(position).isChecked()) {
                items.get(position).setChecked(false);
                filterViewHolder.chbx.setVisibility(View.INVISIBLE);
            } else {
                items.get(position).setChecked(true);
                filterViewHolder.chbx.setVisibility(View.VISIBLE);
            }

            if (filterItemChangeListener != null) {
                filterItemChangeListener.onFilterItemChanged(items.get(position));
            }
        });

        if (items.get(position).getItem() instanceof Cusine) {
            filterViewHolder.title.setText(((Cusine) items.get(position).getItem()).getName());

        } else if (items.get(position).getItem() instanceof Institute) {
            filterViewHolder.title.setText(((Institute) items.get(position).getItem()).getName());
        }

    }

    public interface FilterItemChangeListener {
        void onFilterItemChanged(CheckedItem item);
    }

    @Override
    public int getItemCount() {
        return items.size();
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
