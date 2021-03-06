package com.restamenu.main;

import com.restamenu.base.BaseView;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;

import java.util.List;

/**
 * @author Roodie
 */

public interface MainView extends BaseView<List<Restaurant>> {

    void setSuggestion(List<Restaurant> data);

    void setFoundedRestaurants(List<Restaurant> data);

    void setNearRestaurants(List<Restaurant> data);

    void setInstitutions(List<Institute> data);

    void setCusines(List<Cusine> cusines);
}
