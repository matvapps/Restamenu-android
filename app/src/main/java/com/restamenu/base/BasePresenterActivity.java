package com.restamenu.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.restamenu.R;

/**
 * @author Roodie
 */

public abstract class BasePresenterActivity<P extends Presenter<V>, V, M> extends BaseActivity implements BaseView<M> {

    protected P presenter;

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isLargeLayout = getResources().getBoolean(R.bool.isLargeLayout);
        if(isLargeLayout) {
            // Tablet Mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            // Handset Mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        presenter = (P) getLastCustomNonConfigurationInstance();
        attachPresenter();
    }

    protected abstract void attachPresenter();

    public P getPresenter() {
        return presenter;
    }

}
