package com.restamenu.views.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.restamenu.R;

/**
 * Created by Alexandr
 */

public class NoInternetDialog extends DialogFragment {

//    private static final Handler HANDLER = new Handler(Looper.getMainLooper());


    public void setNoInternetDialogListener(NoInternetDialogListener noInternetDialogListener) {
        this.noInternetDialogListener = noInternetDialogListener;
    }

    NoInternetDialogListener noInternetDialogListener;
    private static boolean shown = false;

    public boolean isRefreshing() {
        return refreshing;
    }

    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
    }

    private boolean refreshing = false;


    public boolean isShown() {
        return shown;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        if (shown) return;

        super.show(manager, tag);
        shown = true;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        shown = false;
        super.onDismiss(dialog);

        if (noInternetDialogListener != null)
            noInternetDialogListener.onDismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_no_internet, null);
        View refreshBtn = view.findViewById(R.id.btn_refresh);

        refreshBtn.setOnClickListener(view1 -> {
            noInternetDialogListener.onRefresh();
            refreshing = true;
            dismiss();
        });

        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    public interface NoInternetDialogListener {
        void onDismiss();

        void onRefresh();
    }

}
