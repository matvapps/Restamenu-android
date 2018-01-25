package com.restamenu.restaurant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.R;
import com.restamenu.util.RestaurantUtils;

import java.util.List;

/**
 * Created by Alexandr.
 */

public class OrderTypeSpinnerAdapter extends ArrayAdapter<ServiceType> {

    private final LayoutInflater mInflater;
    private final List<ServiceType> items;
    private final List<Integer> usingServices;
    private final int selectedItem;

    public OrderTypeSpinnerAdapter(Context context, List<ServiceType> objects, List<Integer> usingServices, int selectedItem) {
        super(context, 0, objects);

        this.selectedItem = selectedItem;
        this.usingServices = usingServices;
        mInflater = LayoutInflater.from(context);
        items = objects;
    }


    @Override
    public boolean isEnabled(int position) {

        if (usingServices.indexOf(position + 1) == -1)
            return false;

        return super.isEnabled(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, R.layout.order_type_dropdown_item, parent);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (usingServices.size() > 0)
            return createItemView(position, R.layout.order_type_header_item, parent);
        else
            return createItemView(position, R.layout.order_type_dropdown_item, parent);
    }

    private View createItemView(int position, int viewId, ViewGroup parent) {

        View view = mInflater.inflate(viewId, parent, false);
        view.setPadding(0, 0, 0, 0);


        TextView titleText = view.findViewById(R.id.order_type_title);
        ImageView serviceImage = view.findViewById(R.id.order_type_image);
        TextView notAvailable = view.findViewById(R.id.order_type_not_available);


        ServiceType service = items.get(position);

        String serviceTitle = RestaurantUtils.getServiceTitle(parent.getContext(), service.getType());

        switch (service.getType()) {
            case 1:
                if (usingServices.indexOf(1) == -1) {
                    view.setEnabled(false);
                    titleText.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.black));
                    serviceImage.setVisibility(View.INVISIBLE);
                    notAvailable.setVisibility(View.VISIBLE);
                } else {
                    if (viewId == R.layout.order_type_header_item)
                        serviceImage.setImageResource(R.drawable.ic_bigrestaurant_link);
                    else {
                        serviceImage.setImageResource(R.drawable.ic_bigrestaurant_off);
                    }
                }
                break;
            case 2:
                if (usingServices.indexOf(2) == -1) {
                    view.setEnabled(false);
                    titleText.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.black));
                    serviceImage.setVisibility(View.INVISIBLE);
                    notAvailable.setVisibility(View.VISIBLE);
                } else{
                    if (viewId == R.layout.order_type_header_item)
                        serviceImage.setImageResource(R.drawable.ic_bigtakeaway_link);
                    else {
                        serviceImage.setImageResource(R.drawable.ic_bigtakeaway_off);
                    }
                }
                break;
            case 3:
                if (usingServices.indexOf(3) == -1) {
                    view.setEnabled(false);
                    titleText.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.black));
                    serviceImage.setVisibility(View.INVISIBLE);
                    notAvailable.setVisibility(View.VISIBLE);

                } else{
                    if (viewId == R.layout.order_type_header_item)
                        serviceImage.setImageResource(R.drawable.ic_bigdelivery_line);
                    else {
                        serviceImage.setImageResource(R.drawable.ic_bigdelivery_off);
                    }
                }
                break;
            default:
                serviceTitle = "";
                break;
        }

        titleText.setText(serviceTitle);


        return view;
    }
}
