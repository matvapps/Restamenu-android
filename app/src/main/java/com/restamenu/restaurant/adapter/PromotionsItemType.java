package com.restamenu.restaurant.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.restamenu.R;
import com.restamenu.model.content.Promotion;

import java.util.List;

/**
 * Created by Alexandr.
 */

/**
 * NOT USED
 */
public class PromotionsItemType extends SampleItemType {

    String title;
    List<Promotion> promotions;


    public PromotionsItemType(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.promotions_item_type;
    }

    @Override
    public int getType() {
        //TODO:
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }


    @Override
    public void bindView(RecyclerView.ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        PromotionsItemType.ViewHolder viewHolder = (PromotionsItemType.ViewHolder) holder;

        ItemAdapter<Promotion> promotionItemAdapter = new ItemAdapter<>();
        FastAdapter fastAdapter = FastAdapter.with(promotionItemAdapter);

        viewHolder.promotionsList.setAdapter(fastAdapter);

        promotionItemAdapter.add(promotions);

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //        TextView titleText;
        RecyclerView promotionsList;

        public ViewHolder(View view) {
            super(view);

//            titleText = view.findViewById(R.id.sections_title);
            promotionsList = view.findViewById(R.id.promotions_list);
            promotionsList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        }
    }

}
