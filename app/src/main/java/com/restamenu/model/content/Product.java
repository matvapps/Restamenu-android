package com.restamenu.model.content;

import java.util.List;

/**
 * @author Roodie
 */

public class Product {

    int product_id;
    String name;
    String description;
    List<String> images;
    List<String> special; //(varchar) массив ключей специальных параметров для товара.
    int price;
    int price_old;
    int price_original;

}
