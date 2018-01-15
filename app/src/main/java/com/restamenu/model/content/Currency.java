package com.restamenu.model.content;

/**
 * Created by Alexandr
 */

public class Currency {

    private int currency_id;
    private String name;
    private String plurals;

    public int getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(int currency_id) {
        this.currency_id = currency_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlurals() {
        return plurals;
    }

    public void setPlurals(String plurals) {
        this.plurals = plurals;
    }
}
