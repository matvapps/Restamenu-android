package com.restamenu.model.content;

/**
 * @author Roodie
 */

public class Category {
    private int category_id;
    private int currency_id;
    private int language_id;
    private String name;
    private String background;
    private String image;
    private List<Product> products;
    private String date; //Дата и время последнего обновления меню в формате Y-m-d H:i:s
}
