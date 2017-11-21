package com.restamenu.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * @author Roodie
 */

public class RestaMenuPreferences {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public RestaMenuPreferences(@NonNull Context context) {
        preferences = context.getSharedPreferences("RestaMenu", 0);
        editor = preferences.edit();
    }


}
