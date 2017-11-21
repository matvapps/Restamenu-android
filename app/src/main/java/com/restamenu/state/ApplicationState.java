package com.restamenu.state;

import com.restamenu.data.RestaMenuPreferences;

/**
 * @author Roodie
 */

public final class ApplicationState {

    private static RestaMenuPreferences preferences;
    private static InMemoryCache memoryCache;

    public ApplicationState() {
    }

    public static RestaMenuPreferences getPreferences() {
        return preferences;
    }

    public static void setPreferences(RestaMenuPreferences preferences) {
        ApplicationState.preferences = preferences;
    }

    public static InMemoryCache getMemoryCache() {
        return memoryCache;
    }

    public static void setMemoryCache(InMemoryCache memoryCache) {
        ApplicationState.memoryCache = memoryCache;
    }
}
