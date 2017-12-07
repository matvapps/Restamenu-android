package com.restamenu.model.responce;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roodie
 */

public class ListResponce<D> {

    private List<D> data;

    @NonNull
    public List<D> getData() {
        if (data == null)
            data = new ArrayList<>();
        return data;
    }
}
