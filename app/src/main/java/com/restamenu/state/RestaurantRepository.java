package com.restamenu.state;

import java.util.List;

/**
 * @author Roodie
 */

public interface RestaurantRepository {

    List<Object> getRestaurants();

    void setRestaurants(List<Object> restaurants);

    Object getRestaunt(String id);
}
