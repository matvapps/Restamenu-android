package com.restamenu.category;

/**
 * Created by Alexandr.
 */

public enum Specials {

    SPICY("Spicy"),
    DISCOUNT("Discount"),
    VEGETARIAN("Vegetarian");

    private final String name;

    Specials(String name) {
        this.name = name;
    }

    public String getType() {
        return name;
    }


}
