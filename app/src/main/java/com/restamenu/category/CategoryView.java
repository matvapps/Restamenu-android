package com.restamenu.category;

import android.support.annotation.NonNull;

import com.restamenu.base.EmptyView;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Language;
import com.restamenu.model.content.Product;

import java.util.List;

/**
 * Created by Alexandr.
 */

public interface CategoryView extends EmptyView<List<Category>>{
    void setLanguages(@NonNull List<Language> languages);

    void setCurrencies(@NonNull List<Currency> currencies);

    void setProducts(@NonNull List<Product> products);
}
