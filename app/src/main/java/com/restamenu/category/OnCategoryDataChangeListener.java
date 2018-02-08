package com.restamenu.category;

import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Language;

/**
 * Created by Alexandr.
 */

public interface OnCategoryDataChangeListener {
    void onPerformSearch(String searchString, int position);
    void onLangChange(Language language, int position);
    void onCurrChange(Currency currency, int position);
}
