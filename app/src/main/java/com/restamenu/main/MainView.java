package com.restamenu.main;

import com.restamenu.base.BaseView;
import com.restamenu.model.content.Restaurant;

import java.util.List;

/**
 * @author Roodie
 */

public interface MainView extends BaseView<List<Restaurant>> {

    void setNearRestaurants(List<Restaurant> data);
}
