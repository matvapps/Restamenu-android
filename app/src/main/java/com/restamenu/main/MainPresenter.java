package com.restamenu.main;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import com.restamenu.R;
import com.restamenu.base.Presenter;
import com.restamenu.loader.LoaderResult;

/**
 * @author Roodie
 */

public class MainPresenter implements Presenter<MainView>, LoaderManager.LoaderCallbacks<LoaderResult> {

    private final LoaderManager loaderManager;
    private MainView view;

    public MainPresenter(LoaderManager loaderManager) {
        this.loaderManager = loaderManager;
    }

    @Override
    public void attachView(MainView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;

    }

    public void loadData() {
        //TODO Put data in bundle if needed to send in loader
        Bundle bundle = new Bundle();
        bundle.putBoolean("key", true);
        loaderManager.restartLoader(R.id.restaurants_loader_id, bundle, this);


    }

    @Override
    public Loader<LoaderResult> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case R.id.restaurants_loader_id: {
                //TODO gat data from bundle if needed
                //return new RestLoader();
                return null;
            }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<LoaderResult> loader, LoaderResult loaderResult) {
        switch (loader.getId()) {
            case R.id.restaurants_loader_id:
                if (loaderResult.getError() != null) {
                    //Do smth in case of error
                } else if (loaderResult.isCompleted()) {
                    //Do smth if completed
                } else {
                    //Do smth on next
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<LoaderResult> loader) {

    }
}
