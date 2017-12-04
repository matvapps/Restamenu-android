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

    public void setFirsrTime(boolean data) {
        editor.putBoolean("is_first_time", data);
        editor.commit();
    }


}
