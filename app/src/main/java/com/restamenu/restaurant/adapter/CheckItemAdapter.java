package com.restamenu.restaurant.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.main.CheckedItem;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Language;
import com.restamenu.restaurant.CheckItemChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandr
 */

public class CheckItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CheckedItem> items;
    private CheckItemChangeListener checkItemChangeListener;
    private int TYPE = 1;

    public static int CURRENCY_TYPE = 1;
    public static int LANGUAGE_TYPE = 2;

    Context context;

    public CheckItemAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<CheckedItem> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void addItem(CheckedItem checkedItem) {
        items.add(checkedItem);
        notifyDataSetChanged();
    }

    public void setItems(List<CheckedItem> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public CheckedItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        this.context = parent.getContext();

        if (TYPE == CURRENCY_TYPE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_currency, parent, false);
            return new CurrencyItemViewHolder(view);

        } else if (TYPE == LANGUAGE_TYPE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_language, parent, false);
            return new LanguageViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (TYPE == CURRENCY_TYPE) {
            CurrencyItemViewHolder currencyViewHolder = (CurrencyItemViewHolder) holder;

            currencyViewHolder.name.setText(((Currency) getItem(position).getItem()).getName());
            currencyViewHolder.icon.setText((((Currency) getItem(position).getItem()).getSymbol()));
            if (getItem(position).isChecked()) {
                currencyViewHolder.name.setTextColor(
                        ContextCompat.getColor(currencyViewHolder.itemView.getContext(), R.color.grey_99));
                currencyViewHolder.icon.setTextColor(
                        ContextCompat.getColor(currencyViewHolder.itemView.getContext(), R.color.grey_99));
                if (!isTablet())
                    currencyViewHolder.check.setVisibility(View.VISIBLE);
            }
            else {
                currencyViewHolder.name.setTextColor(
                        ContextCompat.getColor(currencyViewHolder.itemView.getContext(), R.color.cerulean));
                currencyViewHolder.icon.setTextColor(
                        ContextCompat.getColor(currencyViewHolder.itemView.getContext(), R.color.black));
                currencyViewHolder.check.setVisibility(View.INVISIBLE);
            }
            currencyViewHolder.itemView.setOnClickListener(view -> {
                if (checkItemChangeListener != null) {
                    checkItemChangeListener.onCheckItemSelected(getItem(position));
                    checkItem(position);
                }
            });

        } else if (TYPE == LANGUAGE_TYPE) {
            LanguageViewHolder languageViewHolder = (LanguageViewHolder) holder;

            languageViewHolder.name.setText(((Language) getItem(position).getItem()).getName());
            languageViewHolder.image.setImageResource(getLanguageFlag(((Language) getItem(position).getItem()).getName()));
            if (getItem(position).isChecked()) {
                languageViewHolder.name.setTextColor(
                        ContextCompat.getColor(languageViewHolder.itemView.getContext(), R.color.grey_99));
                if (!isTablet())
                    languageViewHolder.check.setVisibility(View.VISIBLE);
            } else {
                languageViewHolder.name.setTextColor(
                        ContextCompat.getColor(languageViewHolder.itemView.getContext(), R.color.cerulean));
                languageViewHolder.check.setVisibility(View.INVISIBLE);
            }
            languageViewHolder.itemView.setOnClickListener(view -> {
                if (checkItemChangeListener != null) {
                    checkItemChangeListener.onCheckItemSelected(getItem(position));
                    checkItem(position);
                }
            });

        }

    }

    private void checkItem(int position) {
        for (CheckedItem item : items) {
            item.setChecked(false);
        }
        getItem(position).setChecked(true);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public CheckItemChangeListener getCheckItemChangeListener() {
        return checkItemChangeListener;
    }

    public void setCheckItemChangeListener(CheckItemChangeListener checkItemChangeListener) {
        this.checkItemChangeListener = checkItemChangeListener;
    }

    private class LanguageViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView image;
        private ImageView check;

        public LanguageViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.language_name);
            check = itemView.findViewById(R.id.check_image);
            image = itemView.findViewById(R.id.language_icon);

        }
    }


    private class CurrencyItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView icon;
        private ImageView check;

        public CurrencyItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.currency_name);
            check = itemView.findViewById(R.id.check_image);
            icon = itemView.findViewById(R.id.currency_icon);

        }
    }

    public boolean isTablet() {
        return context.getResources().getBoolean(R.bool.isLargeLayout);
    }

    public int getLanguageFlag(String langName) {
        switch (langName) {
            case "English": {
                return R.drawable.ic_flag_uk;
            }
            case "Русский": {
                return R.drawable.ic__flag_russia;
            }
            case "العربية": {
                return R.drawable.ic_flag_arabic;
            }
        }
        return -1;
    }

}
