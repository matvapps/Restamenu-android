package com.restamenu.model.content;

/**
 * Created by Alexandr
 */

public class Language {

    private int language_id;
    private String name;
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
}
