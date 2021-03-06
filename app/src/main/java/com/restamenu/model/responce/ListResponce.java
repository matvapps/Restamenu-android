package com.restamenu.model.responce;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roodie
 */

public class ListResponce<D> {

    @SerializedName("data")
    private List<D> data;

    @NonNull
    public List<D> getData() {
        if (data == null)
            data = new ArrayList<>();
        return data;
    }
}
