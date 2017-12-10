package com.restamenu.restaurant.adapter;

/**
 * @author Roodie
 */

public enum ItemType {

    ORDER_TYPE_PHONE(0),
    ORDER_TYPE_TABLET(1),
    TITLE(2),
    MENU_PHONE(3),
    MENU_TABLET(4),
    PROMOTIONS(5),
    GALLERY(6),
    ABOUT(7),
    CONTACTS_PHONE(8),
    CONTACTS_TABLET(10),
    MAP(9);

    private final int position;

    ItemType(int position) {
        this.position = position;
    }

    public int getType() {
        return position;
    }
}
