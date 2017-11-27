package com.restamenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class NearbyRestaurantListAdapter extends RecyclerView.Adapter<NearbyRestaurantListAdapter.ViewHolder> {

    private List<Restaurant> restaurants;
    private Context context;

    public NearbyRestaurantListAdapter(Context context, List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_restaurant_card, parent, false);

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

    public class ViewHolder extends RecyclerView.ViewHolder{

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