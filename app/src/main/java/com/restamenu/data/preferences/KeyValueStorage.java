package com.restamenu.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * @author Roodie
 */

public class KeyValueStorage {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public KeyValueStorage(@NonNull Context context) {
        preferences = context.getSharedPreferences("RestaMenu", 0);
        editor = preferences.edit();
    }

    public boolean isFirstTime() {
        return preferences.getBoolean("is_first_time", true);
    }

    public void setFirstTime(boolean data) {
        editor.putBoolean("is_first_time", data);
        editor.commit();
    }

    public void setLanguageId(int languageId) {
        editor.putInt("language_id", languageId);
        editor.commit();
    }

    public void setCurrencyId(int currencyId) {
        editor.putInt("currency_id", currencyId);
        editor.commit();
    }

    public int getCurrencyId() {
        return preferences.getInt("currency_id", -1);
    }

    public int getLanguageId() {
        return preferences.getInt("language_id", -1);
    }

}
