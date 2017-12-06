package com.restamenu.model.responce;

import com.restamenu.model.content.Institute;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Roodie
 */

public class InstitutionsResponse {

    private List<Institute> data = new ArrayList<>();

    public List<Institute> getData() {
        return data;
    }
}
