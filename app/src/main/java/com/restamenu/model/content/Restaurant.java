package com.restamenu.model.content;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DEV on 23.11.2017.
 */

public class Restaurant {

    @SerializedName("restaurant_id")
    private int id;
    private String name;

    private List<Integer> cuisine; //массив значений для типов кухни данного ресторана
    private List<Integer> institute; //Mассив значений для типов заведения данного ресторана
    private List<Integer> service; //Массив с идентификаторами типов обслуживания данного ресторана
    private String image;
    private String background;
    private String address;
    private Location location;
    private int distance;
    private int type;  //тип показа карточки категории. Например, 0 - по-умолчанию.

    private String information;
    private List<String> timing;
    private List<String> phones;
    private List<String> social;
    private List<Integer> currency;
    private List<Integer> languages;

    //private String street;
    //private int distance;


    public Restaurant() {
    }

    public List<Integer> getCuisine() {
        return cuisine;
    }

    public List<Integer> getInstitute() {
        return institute;
    }

    public String getImage() {
        return image;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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



