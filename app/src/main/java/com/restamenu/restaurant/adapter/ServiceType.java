package com.restamenu.restaurant.adapter;

/**
 * Created by Alexandr.
 */

public enum ServiceType {

    DELIVERY(3),
    TAKEAWAY(2),
    RESTAURANT(1);

    private final int position;

    ServiceType(int position)
    {
        this.position = position;
    }

    public int getType() {
        return position;
    }

}
