package com.restamenu.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.restamenu.BuildConfig;
import com.restamenu.R;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;
import com.restamenu.restaurant.RestaurantActivity;
import com.restamenu.util.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class NearbyRestaurantListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Restaurant> restaurants;
    private Context context;
    private List<Institute> instituteList;
    private View.OnClickListener clickListener;
    private boolean useScrollIt;
    private boolean usedFirstElement;


    public Restaurant getItem(int position) {
        return restaurants.get(position);
    }

    public NearbyRestaurantListAdapter(Context context) {
        this.restaurants = new ArrayList<>();
        this.context = context;
        this.clickListener = null;
        this.useScrollIt = false;
        this.usedFirstElement = false;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;

        Logger.log(viewType + "");

        if (viewType == 0 && useScrollIt) {
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scroll_it_item_white, parent, false);

            usedFirstElement = true;
            useScrollIt = false;

            return new ScrollItViewHolder(rootView);
        } else {
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.restaurant_card_nearby, parent, false);
        }

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Restaurant restaurant = restaurants.get(position);

        if (holder instanceof ScrollItViewHolder)
            return;

        ViewHolder cardHolder = (ViewHolder) holder;

        cardHolder.restaurantTitleTextView.setText(restaurants.get(position).getName());
        cardHolder.restaurantStreetTextView.setText(restaurants.get(position).getAddress());
        cardHolder.restaurantDistanceTextView.setText((int) (restaurants.get(position).getDistance()) + "m");

        String path = BuildConfig.BASE_URL + restaurant.getImage().substring(1, restaurant.getImage().length());

        Picasso.with(context).load(path).into(cardHolder.restaurantBackgroundImageView);


        for (int i = 0; i < restaurant.getServices().size(); i++) {
            switch (restaurant.getServices().get(i)) {
                //restaurant
                case 1: {
                    Picasso.with(context).load(R.drawable.ic_restoran_active).into(cardHolder.foodTypeRestaurantImageView);
                    break;
                }
                //takeaway
                case 2: {
                    Picasso.with(context).load(R.drawable.ic_food_active).into(cardHolder.foodTypeTakeawayImageView);
                    break;
                }
                //delivery
                case 3: {
                    Picasso.with(context).load(R.drawable.ic_delivery_active).into(cardHolder.foodTypeDeliveryImageView);
                    break;
                }
            }
        }


        StringBuilder institutions = new StringBuilder();
        for (int i = 0; i < restaurant.getInstitutes().size(); i++) {
            if (i < restaurant.getInstitutes().size() - 1 && instituteList != null)
                institutions.append(getInstituteName(restaurant.getInstitutes().get(i))).append(" & ");
            else
                institutions.append(getInstituteName(restaurant.getInstitutes().get(i))).append("");
        }

        cardHolder.restaurantTypeTextView.setText(institutions.toString());

        if (clickListener != null)
            cardHolder.rootView.setOnClickListener(clickListener);
        else {

            cardHolder.rootView.setOnClickListener(view -> {
                Intent intent = new Intent(context, RestaurantActivity.class);
                intent.putExtra(RestaurantActivity.KEY_RESTAURANT_ID, restaurant.getId());

                context.startActivity(intent);
            });

        }


    }


    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public List<Institute> getInstituteList() {
        return instituteList;
    }

    public void setInstituteList(List<Institute> instituteList) {
        this.instituteList = instituteList;
        notifyDataSetChanged();

    }

    private String getInstituteName(int instituteId) {
        for (int i = 0; i < instituteList.size(); i++) {
            if (instituteList.get(i).getId() == instituteId)
                return instituteList.get(i).getName();
        }
        return "";
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public boolean isUseScrollIt() {
        return useScrollIt;
    }

    public void setUseScrollIt(boolean useScrollIt) {
        this.useScrollIt = useScrollIt;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View rootView;
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

            rootView = itemView;
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

    class ScrollItViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView text;

        public ScrollItViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.scroll_it_image);
            text = itemView.findViewById(R.id.scroll_it_text);

        }

    }

}
