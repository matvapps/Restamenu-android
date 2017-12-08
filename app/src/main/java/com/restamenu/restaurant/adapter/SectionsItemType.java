package com.restamenu.restaurant.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.restamenu.R;
import com.restamenu.model.content.Category;

import java.util.List;

/**
 * Created by Alexandr.
 */

/**
 * NOT USED
 */
public class SectionsItemType extends SampleItemType {

    private String titleText;
    private List<Category> sectionsList;

    public SectionsItemType(List<Category> sectionsList) {
        this.sectionsList = sectionsList;
    }

    public SectionsItemType(String titleText, List<Category> sectionsList) {
        this.titleText = titleText;
        this.sectionsList = sectionsList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.sections_item_type;
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

        ViewHolder viewHolder = (ViewHolder) holder;

        ItemAdapter<Category> categoryItemAdapter = new ItemAdapter<>();
        FastAdapter fastAdapter = FastAdapter.with(categoryItemAdapter);

        viewHolder.sectionsList.setAdapter(fastAdapter);

        categoryItemAdapter.add(sectionsList);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //        TextView titleText;
        RecyclerView sectionsList;

        public ViewHolder(View view) {
            super(view);

//            titleText = view.findViewById(R.id.sections_title);
            sectionsList = view.findViewById(R.id.sections_list);
            sectionsList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        }
    }
}
