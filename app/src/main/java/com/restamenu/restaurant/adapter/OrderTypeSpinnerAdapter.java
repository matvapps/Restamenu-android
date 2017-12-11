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

import java.util.List;

/**
 * Created by Alexandr.
 */

public class OrderTypeSpinnerAdapter extends ArrayAdapter<ServiceType> {

    private final LayoutInflater mInflater;
    private final List<ServiceType> items;
    private final List<Integer> usingServices;

    public OrderTypeSpinnerAdapter(Context context, List<ServiceType> objects, List<Integer> usingServices) {
        super(context, 0, objects);

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
            return createItemView(position, R.layout.order_type_item, parent);
        else
            return createItemView(position, R.layout.order_type_dropdown_item, parent);
    }

    private View createItemView(int position, int viewId, ViewGroup parent) {

        View view = mInflater.inflate(viewId, parent, false);

//        if (position == 0) {
//            view = mInflater.inflate(R.layout.order_type_item, parent, false);
//        }

        TextView titleText = view.findViewById(R.id.order_type_title);
        ImageView serviceImage = view.findViewById(R.id.order_type_image);
        TextView notAvailable = view.findViewById(R.id.order_type_not_available);


        ServiceType service = items.get(position);

        String serviceTitle;

        switch (service.getType()) {
            case 1:
                serviceTitle = parent.getContext().getResources().getString(R.string.service_restaurant);
                if (usingServices.indexOf(1) == -1) {
                    view.setEnabled(false);
                    titleText.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.warm_grey_66));
                    serviceImage.setVisibility(View.INVISIBLE);
                    notAvailable.setVisibility(View.VISIBLE);
                } else
                    serviceImage.setImageResource(R.drawable.ic_restoran_noactive);
                break;
            case 2:
                serviceTitle = parent.getContext().getResources().getString(R.string.service_takeaway);
                if (usingServices.indexOf(2) == -1) {
                    view.setEnabled(false);
                    titleText.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.warm_grey_66));
                    serviceImage.setVisibility(View.INVISIBLE);
                    notAvailable.setVisibility(View.VISIBLE);
                } else
                    serviceImage.setImageResource(R.drawable.ic_food_noactive);
                break;
            case 3:
                serviceTitle = parent.getContext().getResources().getString(R.string.service_delivery);
                if (usingServices.indexOf(3) == -1) {
                    view.setEnabled(false);
                    titleText.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.warm_grey_66));
                    serviceImage.setVisibility(View.INVISIBLE);
                    notAvailable.setVisibility(View.VISIBLE);

                } else
                    serviceImage.setImageResource(R.drawable.ic_deliver_noactive);
                break;
            default:
                serviceTitle = "";
                break;
        }

        titleText.setText(serviceTitle);


        return view;
    }
}
