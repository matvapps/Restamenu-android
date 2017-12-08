package com.restamenu.restaurant.adapter;

/**
 * @author Roodie
 */

public class AdapterItemType<D> {

    private String title;
    private D data;
    private ItemType type;
    //TODO may be added interface, to perform onClick events

    public AdapterItemType(String title, D data, ItemType type) {
        this.title = title;
        this.data = data;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public D getData() {
        return data;
    }

    public ItemType getType() {
        return type;
    }
}
