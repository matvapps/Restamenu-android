package com.restamenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.model.content.Restaurant;

import java.util.ArrayList;
import java.util.List;


public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private List<Restaurant> restaurants;
    private Context context;

    public RestaurantListAdapter(Context context) {
        this.restaurants = new ArrayList<>();
        this.context = context;
    }

    public void setData(List<Restaurant> data) {
        this.restaurants.clear();
        this.restaurants.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {

        return position;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        switch (viewType) {
            case 1:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.advertising_restaurant_item_card, parent, false);
                break;
            default:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_item_card, parent, false);
                break;
        }


        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        if(restaurants.get(position).getTitle() != null) {
//
//            holder.restaurantBackgroundImageView.setImageDrawable(
//                    ContextCompat.getDrawable(context, R.drawable.rest_background_test));
//            holder.restaurantTitleTextView.setText(restaurants.get(position).getTitle());
//            holder.restaurantTypeTextView.setText(restaurants.get(position).getType());
//            holder.restaurantStreetTextView.setText(restaurants.get(position).getStreet());
//            holder.restaurantDistanceTextView.setText(
//                    String.valueOf(restaurants.get(position).getDistance()));
//        }

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView restaurantBackgroundImageView;
        TextView restaurantTitleTextView;
        TextView restaurantTypeTextView;
        ImageView foodTypeRestaurantImageView;
        ImageView foodTypeTakeawayImageView;
        ImageView foodTypeDeliveryImageView;
        TextView restaurantStreetTextView;
        TextView restaurantDistanceTextView;


        public ViewHolder(View itemView) {
            super(itemView);

            restaurantBackgroundImageView = itemView.findViewById(R.id.restaurant_background);
            restaurantTitleTextView = itemView.findViewById(R.id.restaurant_title);
            restaurantTypeTextView = itemView.findViewById(R.id.restaurant_type);
            foodTypeRestaurantImageView = itemView.findViewById(R.id.food_type_restaurant_img);
            foodTypeTakeawayImageView = itemView.findViewById(R.id.food_type_takeaway_img);
            foodTypeDeliveryImageView = itemView.findViewById(R.id.food_type_delivery_img);
            restaurantStreetTextView = itemView.findViewById(R.id.restaurant_street);
            restaurantDistanceTextView = itemView.findViewById(R.id.distance_to_restaurant);

        }
    }
}
