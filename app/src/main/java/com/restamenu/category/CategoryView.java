package com.restamenu.category;

import android.support.annotation.NonNull;

import com.restamenu.model.content.Product;

import java.util.List;

/**
 * Created by Alexandr.
 */

public interface CategoryView {

    void setProducts(@NonNull List<Product> products);

}
