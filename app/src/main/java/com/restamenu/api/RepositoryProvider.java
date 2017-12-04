package com.restamenu.api;

import com.restamenu.data.preferences.KeyValueStorage;

/**
 * @author Roodie
 */

public final class RepositoryProvider {

    private static KeyValueStorage preferences;
    private static ApplicationRepository appRepository;

    public RepositoryProvider() {
    }

    public static KeyValueStorage getPreferences() {
        return preferences;
    }

    public static void setPreferences(KeyValueStorage preferences) {
        RepositoryProvider.preferences = preferences;
    }

    public static ApplicationRepository getAppRepository() {
        return appRepository;
    }

    public static void setAppRepository(ApplicationRepository appRepository) {
        RepositoryProvider.appRepository = appRepository;
    }
}
