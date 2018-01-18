package com.restamenu.model.content;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexandr
 */

public class Currency {

    private int currency_id;
    private String name;
    private String plurals;

    @SerializedName("short")
    private String shortName;
    private String symbol;
    private float rate;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

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
