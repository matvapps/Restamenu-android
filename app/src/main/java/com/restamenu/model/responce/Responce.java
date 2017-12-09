package com.restamenu.model.responce;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * @author Roodie
 */

public class Responce<D> {

    @SerializedName("data")
    private D data;

    @NonNull
    public D getItem() {
        return data;
    }
}
