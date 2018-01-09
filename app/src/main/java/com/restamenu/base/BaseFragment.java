package com.restamenu.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.restamenu.views.dialog.LoadingView;

/**
 * Created by alessio on 05.01.2018.
 */

public abstract class BaseFragment extends Fragment {
    public LoadingView loadingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //loadingView = LoadingDialog.view(getFragmentManager());

        View view = inflater.inflate(getLayoutResource(), container, false);

        initViews(view);

        return view;
    }

    @LayoutRes
    protected abstract int getLayoutResource();

    protected abstract void initViews(View view);

}
