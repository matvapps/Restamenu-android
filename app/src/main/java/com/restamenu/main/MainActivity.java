package com.restamenu.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.restamenu.R;
import com.restamenu.base.BaseNavigationActivity;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseNavigationActivity<MainPresenter, MainView, List<Object>> implements MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

    @Override
    protected void attachPresenter() {
        if (presenter == null) {
            presenter = new MainPresenter(getLoaderManager());
        }
        presenter.attachView(this);

    }

    @Override
    protected int getContentViewLayoutId() {
        return 0;
    }

    @Override
    public void setData(@NonNull List<Object> data) {
        //TODO
    }

    @Override
    public void showError() {
        //TODO
    }

    @Override
    public void showLoading(boolean show) {
        //TODO
    }

}
