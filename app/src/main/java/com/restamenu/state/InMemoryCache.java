package com.restamenu.state;


import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Roodie
 */

public class InMemoryCache implements ProfileRepository, RestaurantRepository {

    private Map<String, Object> restaurants;

    public InMemoryCache() {
        restaurants = new ArrayMap<>(50);
    }

    @Override
    public List<Object> getRestaurants() {
        return new ArrayList<>(restaurants.values());
    }

    @Override
    public void setRestaurants(List<Object> restaurants) {
        for (Object rest : restaurants)
            this.restaurants.put("id", rest);
    }

    @Override
    public Object getRestaunt(String id) {
        Object rest = restaurants.get(id);
        return rest;
    }
}
