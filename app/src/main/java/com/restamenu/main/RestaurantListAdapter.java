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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant> restaurants;
    private List<Institute> instituteList;
    private RestaurantClickListener listener;

    public RestaurantListAdapter(Context context, RestaurantClickListener listener) {
        this.restaurants = new ArrayList<>();
        this.instituteList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<Restaurant> data) {
        this.restaurants.clear();
        this.restaurants.addAll(data);
        notifyDataSetChanged();
    }

    public Restaurant getItem(int position){
        return restaurants.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;

        switch (restaurants.get(viewType).getType()) {
            case 0:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_card_item, parent, false);
                break;
            default:
                rootView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_advertising_card_item, parent, false);
                break;
        }


        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Restaurant item = restaurants.get(position);
//        Restaurant restaurant = restaurants.get(position);

        holder.restaurantTitleTextView.setText(item.getName());
        holder.itemView.setOnClickListener(click -> listener.onRestaurantClicked(item.getId()));

//        WindowManager windowManager = (WindowManager) context
//                .getSystemService(Context.WINDOW_SERVICE);

//        Display display = windowManager.getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics ();
//        display.getMetrics(outMetrics);
//
//        float density  = context.getResources().getDisplayMetrics().density;
//        float dpWidth  = outMetrics.widthPixels / density;

        String path = BuildConfig.BASE_URL + item.getImage();

        Picasso.with(context).load(path).into(holder.restaurantBackgroundImageView);

        for (int i = 0; i < item.getServices().size(); i++) {
            switch (item.getServices().get(i)) {
                //restaurant
                case 1: {
                    Picasso.with(context).load(R.drawable.ic_restoran_active).into(holder.foodTypeRestaurantImageView);
                    break;
                }
                //takeaway
                case 2: {
                    Picasso.with(context).load(R.drawable.ic_food_active).into(holder.foodTypeTakeawayImageView);
                    break;
                }
                //delivery
                case 3: {
                    Picasso.with(context).load(R.drawable.ic_delivery_active).into(holder.foodTypeDeliveryImageView);
                    break;
                }
            }
        }

        StringBuilder institutions = new StringBuilder();
        for (int i = 0; i < item.getInstitutes().size(); i++) {
            if (i < item.getInstitutes().size() - 1)
                institutions.append(getInstituteName(item.getInstitutes().get(i))).append(", ");
            else
                institutions.append(getInstituteName(item.getInstitutes().get(i))).append("");
        }

        holder.restaurantTypeTextView.setText(institutions.toString());

        holder.rootView.setOnClickListener(view -> {
            Intent intent = new Intent(context, RestaurantActivity.class);
            intent.putExtra(RestaurantActivity.KEY_RESTAURANT_ID, item.getId());

            context.startActivity(intent);
        });


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
    }


    private String getInstituteName(int instituteId) {
        for (int i = 0; i < instituteList.size(); i++) {
            if (instituteList.get(i).getId() == instituteId)
                return instituteList.get(i).getName();
        }
        return "";
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
}
