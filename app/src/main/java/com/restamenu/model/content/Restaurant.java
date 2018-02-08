package com.restamenu.model.content;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DEV on 23.11.2017.
 */

public class Restaurant {

    @SerializedName("restaurant_id")
    private String id;
    private String name;

    @SerializedName("cusine")
    private List<Integer> cusine; //массив значений для типов кухни данного ресторана
    @SerializedName("institute")
    private List<Integer> institute; //Mассив значений для типов заведения данного ресторана
    @SerializedName("service")
    private List<Integer> service; //Массив с идентификаторами типов обслуживания данного ресторана
    private String image;
    private String background;
    private String address;
    private Location location;
    private float distance;
    private int type;  //тип показа карточки категории. Например, 0 - по-умолчанию.

    private String information;
//    private List<String> timing;
    private List<String> phones;
    private List<String> social;
    @SerializedName("currency")
    private int[] currencies;
    @SerializedName("language")
    private int[] languages;

    private boolean detailsFetched = false;

    //private String street;
    //private int distance;


    public Restaurant() {
    }

    public int getId() {
        return Integer.valueOf(id);
    }

    public String getName() {
        return name;
    }

    public List<Integer> getCusines() {
        return cusine;
    }

    public List<Integer> getInstitutes() {
        return institute;
    }

    public List<Integer> getServices() {
        if (service == null)
            service = new ArrayList<>();
        return service;
    }

    public String getImage() {
        return image;
    }

    public String getBackground() {
        return background;
    }

    public String getAddress() {
        return address;
    }

    public Location getLocation() {
        return location;
    }

    public float getDistance() {
        return distance;
    }

    public int getType() {
        return type;
    }

    public String getInformation() {
        return information;
    }

//    public List<String> getTiming() {
//        return timing;
//    }

    public List<String> getPhones() {
        return phones;
    }

    public List<String> getSocial() {
        return social;
    }

    public int[] getCurrencies() {
        return currencies;
    }

    public int[] getLanguages() {
        return languages;
    }

    public boolean isDetailsFetched() {
        return detailsFetched;
    }

    public void setDetailsFetched(boolean detailsFetched) {
        this.detailsFetched = detailsFetched;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Restaurant{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }

}


