package com.restamenu.views.setting;

import com.restamenu.model.content.Currency;
import com.restamenu.model.content.Language;

/**
 * Created by Alexandr.
 */

public interface OnSettingItemChanged {
    void onLanguageChanged(Language language);
    void onCurrencyChanged(Currency currency);
}
