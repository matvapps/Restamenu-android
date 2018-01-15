package com.restamenu.restaurant;

import android.support.annotation.NonNull;

import com.restamenu.base.EmptyView;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Image;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Language;
import com.restamenu.model.content.Promotion;
import com.restamenu.model.content.Restaurant;

import java.util.List;

/**
 * @author Roodie
 */

public interface RestaurantView extends EmptyView<Restaurant> {

    void setLanguages(@NonNull List<Language> languages);

    void setCurrencies(@NonNull List<Currency> currencies);

    void setCategories(@NonNull List<Category> categories);

    void setGallery(@NonNull List<Image> images);

    void setPromotions(@NonNull List<Promotion> promotions);

    void setInstitutions(@NonNull List<Institute> institutions);

}
