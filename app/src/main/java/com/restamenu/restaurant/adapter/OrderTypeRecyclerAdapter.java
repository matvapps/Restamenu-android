package com.restamenu.restaurant.adapter;

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

public class OrderTypeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ServiceType> items;

    public OrderTypeRecyclerAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<ServiceType> data) {
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setItems(List<ServiceType> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_type_item, parent, false);

        return new ServiceTypeViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ServiceTypeViewHolder serviceTypeViewHolder = (ServiceTypeViewHolder) holder;




    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ServiceTypeViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        View mainView;
        ImageView icon;
        View selectedContainer;
        View notAvailable;

        public ServiceTypeViewHolder(View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.order_type_title);
            mainView = itemView.findViewById(R.id.order_type_view);
            icon = itemView.findViewById(R.id.order_type_image);
            selectedContainer = itemView.findViewById(R.id.order_type_selected_container);
            notAvailable = itemView.findViewById(R.id.order_type_not_available);

            mainView.setOnClickListener(view -> {

            });

        }
    }

}