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


}
