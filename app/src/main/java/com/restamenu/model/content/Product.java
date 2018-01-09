package com.restamenu.model.content;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roodie
 */

public class Product {

    private int product_id;
    private String name;
    private String description;
    private List<String> images;
    private List<String> special; //(varchar) массив ключей специальных параметров для товара.
    private int price;
    private int price_old;
    private int price_original;

    public int getId() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImages() {
        if (images == null)
            images = new ArrayList<>();
        return images;
    }

    public List<String> getSpecial() {
        if (special == null)
            special = new ArrayList<>();
        return special;
    }

    public int getPrice() {
        return price;
    }

    public int getPrice_old() {
        return price_old;
    }

    public int getPriceOriginal() {
        return price_original;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Product{");
        sb.append("product_id=").append(product_id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }
}
