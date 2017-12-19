package com.restamenu.category;

/**
 * Created by Alexandr.
 */

public enum Specials {

    SPICY("spicy"),
    DISCOUNT("discount"),
    VEGETARIAN("vegetarian");

    private final String name;

    Specials(String name) {
        this.name = name;
    }

    public String getType() {
        return name;
    }


}
