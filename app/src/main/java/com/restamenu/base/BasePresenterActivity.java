package com.restamenu.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

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

        presenter = (P) getLastCustomNonConfigurationInstance();
        attachPresenter();
    }

    protected abstract void attachPresenter();

    public P getPresenter() {
        return presenter;
    }

    @Override
    public void showLoading(boolean show) {
        if (show)
            loadingView.showLoadingIndicator();
        else
            loadingView.hideLoadingIndicator();
    }

    @Override
    public void showError() {
        loadingView.hideLoadingIndicator();
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }
}
