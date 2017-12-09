package com.restamenu.model.content;

import java.util.List;

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
    private String modified;


    public int geId() {
        return category_id;
    }

    public int getCurrencyId() {
        return currency_id;
    }

    public int getLanguageId() {
        return language_id;
    }

    public String getName() {
        return name;
    }

    public String getBackground() {
        return background;
    }

    public String getImage() {
        return image;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Category{");
        sb.append("category_id=").append(category_id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
