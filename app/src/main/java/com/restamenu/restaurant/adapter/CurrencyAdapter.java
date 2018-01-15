package com.restamenu.restaurant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.model.content.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr
 */

public class CurrencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Currency> items;

    public CurrencyAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<Currency> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<Currency> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public Currency getItem(int position) {
        return items.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_currency, parent,false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CurrencyViewHolder currencyViewHolder = (CurrencyViewHolder) holder;

        currencyViewHolder.name.setText(getItem(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class CurrencyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView image;

        public CurrencyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.currency_name);
//            image = itemView.findViewById(R.id.language_image);

        }
    }

}
