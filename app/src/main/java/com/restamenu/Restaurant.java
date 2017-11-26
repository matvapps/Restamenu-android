package com.restamenu;

/**
 * Created by DEV on 23.11.2017.
 */

public class Restaurant {

    private String backgroundImageUrl;
    private String title;
    private String type;
    private String[] foodTypes;
    private String street;
    private int distance;

    public Restaurant() {
    }

    public Restaurant(String backgroundImageUrl, String title, String type, String[] foodTypes, String street, int distance) {
        this.title = title;
        this.type = type;
        this.foodTypes = foodTypes;
        this.street = street;
        this.distance = distance;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getFoodTypes() {
        return foodTypes;
    }

    public void setFoodTypes(String[] foodTypes) {
        this.foodTypes = foodTypes;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
