package com.restamenu.model.responce;

import com.restamenu.model.content.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roodie
 */

public class RestaurantsResponce {

    private List<Restaurant> restaurants = new ArrayList<>();

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
