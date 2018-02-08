package com.restamenu.model.content;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexandr
 */

public class Language {

    private int language_id;
    private String name;
    @SerializedName("code")
    private String shortName;
    private char side;

    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
