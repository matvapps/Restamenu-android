package com.restamenu.api;

import com.restamenu.data.database.LocalRepository;
import com.restamenu.data.preferences.KeyValueStorage;

/**
 * @author Roodie
 */

public final class RepositoryProvider {

    private static KeyValueStorage preferences;
    private static LocalRepository localRepository;
    private static RemoteRepository remoteRepository;

    public RepositoryProvider() {
    }

    public static KeyValueStorage getPreferences() {
        return preferences;
    }

    public static void setPreferences(KeyValueStorage preferences) {
        RepositoryProvider.preferences = preferences;
    }

    public static LocalRepository getLocalRepository() {
        return localRepository;
    }

    public static void setLocalRepository(LocalRepository localRepository) {
        RepositoryProvider.localRepository = localRepository;
    }

    public static RemoteRepository getRemoteRepository() {
        return remoteRepository;
    }

    public static void setRemoteRepository(RemoteRepository remoteRepository) {
        RepositoryProvider.remoteRepository = remoteRepository;
    }
}
