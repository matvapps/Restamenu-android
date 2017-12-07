package com.restamenu.data.database;

import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Institute;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Concrete implementation of a data source as a db.
 */
public class LocalRepository {

    //private final int MAP_SIZE = 50;
    //private SparseArray<Restaurant> restaurants;
    //private SparseArray<Restaurant> nearRestaurants;
    //private SparseArray<Institute> institutions;

    public LocalRepository() {

        //restaurants = new SparseArray<>(MAP_SIZE);
        //nearRestaurants = new SparseArray<>(MAP_SIZE);
        //institutions = new SparseArray<>();
    }

    public void saveInstitution(Institute instition) {
        //TODO save to DB
    }

    public Flowable<List<Institute>> getInstitutions() {
        return Flowable.just(new ArrayList<Institute>());
    }

    public void saveCusine(Cusine cuisine) {
        //TODO save to DB
    }

    public Flowable<List<Cusine>> getCusines() {
        return Flowable.just(new ArrayList<Cusine>());
    }


    public void logout() {

    }
}
