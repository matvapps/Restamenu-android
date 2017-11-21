package com.restamenu.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Roodie
 */

public class LoaderResult<T> {

    private T result;

    private Throwable error;

    private boolean isComplete;


    public LoaderResult(T result, Throwable error, boolean isComplete) {
        this.result = result;
        this.error = error;
        this.isComplete = isComplete;
    }

    @NonNull
    public static <T> LoaderResult<T> onNext(@Nullable T result) {
        return new LoaderResult<>(result, null, false);
    }

    @NonNull
    public static <T> LoaderResult<T> onError(@Nullable Throwable error) {
        return new LoaderResult<>(null, error, false);
    }

    @NonNull
    public static <T> LoaderResult<T> onComplete() {
        return new LoaderResult<>(null, null, true);
    }

    @Nullable
    public T getResult() {
        return result;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public boolean isCompleted() {
        return isComplete;
    }

}
