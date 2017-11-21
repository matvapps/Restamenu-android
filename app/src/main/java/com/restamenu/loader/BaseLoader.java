package com.restamenu.loader;

import android.content.Context;
import android.content.Loader;

/**
 * @author Roodie
 */

public class BaseLoader extends Loader<LoaderResult> {

    private LoaderResult result;


    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(LoaderResult data) {
        if (isReset()) {
            return;
        }
        result = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (result != null) {
            deliverResult(result);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        result = null;
    }

}
