package com.restamenu.base;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.restamenu.R;
import com.restamenu.RestamenuApp;
import com.restamenu.util.ConnectionReceiver;
import com.restamenu.util.Connectivity;
import com.restamenu.views.dialog.NoInternetDialog;

/**
 * @author Roodie
 */

public abstract class BasePresenterActivity<P extends Presenter<V>, V, M> extends BaseActivity implements BaseView<M>,
        ConnectionReceiver.ConnectionReceiverListener{

    protected P presenter;
    protected AlertDialog dialog;
    protected NoInternetDialog noInternetDialog;

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
        dialog = buildDialog(this).create();
        noInternetDialog = new NoInternetDialog();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        attachPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        RestamenuApp.get().setConnectionListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    protected abstract void attachPresenter();

    public P getPresenter() {
        return presenter;
    }



    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        View view  = getLayoutInflater().inflate(R.layout.fragment_no_internet, null);

        builder.setView(view);
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });

        return builder;
    }

    void showNoInternetDialog() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(NoInternetDialog.class.getSimpleName());
        if (prev != null) {
//            ft.remove(prev);
        } else {

            // Create and show the dialog.
            NoInternetDialog newFragment = new NoInternetDialog();
            newFragment.show(ft, NoInternetDialog.class.getSimpleName());
        }

    }


    @Override
    public void showLoading(boolean show) {
        if (show) {
            if (noInternetDialog.isShown())
                noInternetDialog.dismiss();
            loadingView.showLoadingIndicator();

        } else {
            loadingView.hideLoadingIndicator();
        }
    }

    @Override
    public void showError() {
        loadingView.hideLoadingIndicator();
        if (!noInternetDialog.isShown() && !Connectivity.isConnected(BasePresenterActivity.this))
            noInternetDialog.show(getSupportFragmentManager(), NoInternetDialog.class.getSimpleName());

    }
}
