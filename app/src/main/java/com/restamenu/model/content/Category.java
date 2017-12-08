package com.restamenu.model.content;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.restamenu.R;

import java.util.List;

/**
 * @author Roodie
 */

public class Category extends AbstractItem<Category, Category.ViewHolder>{
    private int category_id;
    private int currency_id;
    private int language_id;
    private String name;
    private String background;
    private String image;
    private List<Product> products;
    private String date; //Дата и время последнего обновления меню в формате Y-m-d H:i:s


    public Category(String name) {
        this.name = name;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        //TODO:
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.restaurant_dish_category_item;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        holder.title.setText(name);

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView background;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.category_name);
            background = view.findViewById(R.id.category_background);

        }
    }



}
