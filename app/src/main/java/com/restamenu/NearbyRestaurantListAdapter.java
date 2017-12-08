package com.restamenu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.model.content.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class NearbyRestaurantListAdapter extends RecyclerView.Adapter<NearbyRestaurantListAdapter.ViewHolder> {

    private List<Restaurant> restaurants;

    public NearbyRestaurantListAdapter() {
        this.restaurants = new ArrayList<>();
    }

    public void setData(List<Restaurant> data) {
        this.restaurants.clear();
        this.restaurants.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_restaurant_card, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Restaurant restaurant = restaurants.get(position);

        holder.restaurantTitleTextView.setText(restaurant.getName());
        holder.restaurantStreetTextView.setText(restaurant.getAddress());
        holder.restaurantDistanceTextView.setText(restaurant.getDistance() + "m");
        Picasso.with(holder.itemView.getContext()).load(R.drawable.restaurants).into(holder.restaurantBackgroundImageView);

        for (int i = 0; i < restaurant.getService().size(); i++) {
            switch (restaurant.getService().get(i)) {
                //restaurant
                case 1: {
                    Picasso.with(holder.itemView.getContext()).load(R.drawable.ic_restoran_active).into(holder.foodTypeRestaurantImageView);
                    break;
                }
                //takeaway
                case 2: {
                    Picasso.with(holder.itemView.getContext()).load(R.drawable.ic_food_active).into(holder.foodTypeTakeawayImageView);
                    break;
                }
                //delivery
                case 3: {
                    Picasso.with(holder.itemView.getContext()).load(R.drawable.ic_deliver_active).into(holder.foodTypeDeliveryImageView);
                    break;
                }
            }
        }

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
