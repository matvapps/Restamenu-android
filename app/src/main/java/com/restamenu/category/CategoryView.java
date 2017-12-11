package com.restamenu.category;

import com.restamenu.base.EmptyView;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Product;

import java.util.List;

/**
 * Created by Alexandr.
 */

public interface CategoryView extends EmptyView<List<Category>>{

    void setProducts(List<Product> products);
}
