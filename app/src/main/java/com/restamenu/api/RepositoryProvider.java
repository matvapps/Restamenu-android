package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.data.preferences.KeyValueStorage;

/**
 * @author Roodie
 */

public final class RepositoryProvider {

    private static KeyValueStorage sPreferences;
    private static ApplicationRepository sAppRepository;

    public RepositoryProvider() {
    }

    public static KeyValueStorage getPreferences() {
        return sPreferences;
    }

    public static void setPreferences(@NonNull KeyValueStorage preferences) {
        sPreferences = preferences;
    }

    public static ApplicationRepository getAppRepository() {
        return sAppRepository;
    }

    public static void setAppRepository(@NonNull ApplicationRepository appRepository) {
        sAppRepository = appRepository;
    }
}
