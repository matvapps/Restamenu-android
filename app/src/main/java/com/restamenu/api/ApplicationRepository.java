package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.data.database.LocalRepository;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Cusine;
import com.restamenu.model.content.Institute;
import com.restamenu.model.content.Restaurant;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * @author Roodie
 */

public class ApplicationRepository implements DataSource {

    private final LocalRepository localRepository;
    private final RemoteRepository remoteRepository;

    private Map<Integer, Restaurant> restaurants;
    private Map<Integer, Restaurant> nearRestaurants;
    private Map<Integer, Institute> institutions;
    private Map<Integer, Cusine> cusines;

    private boolean cusinesAreValid = false;
    private boolean institutionsAreValid = false;


    public ApplicationRepository(@NonNull LocalRepository localRepository, @NonNull RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;

        restaurants = new HashMap<>(50);
        nearRestaurants = new HashMap<>(50);
        //institutions = new HashMap<>(10);
        //cusines = new HashMap<>(10);
    }

    @Override
    public Flowable<List<Restaurant>> getRestaurants(@NonNull Integer cityId, int page) {
        //Try to fetch from local db. If empty - from remote server.
        restaurants.clear();
        return remoteRepository
                .getRestaurants(cityId, page)
                .flatMap(tasks -> Flowable.fromIterable(tasks).doOnNext(restaurant -> {
                    restaurants.put(restaurant.getId(), restaurant);
                }).toList().toFlowable());
    }

    @Override
    public Flowable<List<Restaurant>> getNearRestaurants(@NonNull Integer cityId, @NonNull String geo) {
        return remoteRepository.getNearRestaurants(cityId, geo, true);
    }

    @Override
    public Single<Restaurant> getRestaurant(@NonNull Integer id) {
        return remoteRepository.getRestaurant(id);
    }

    @Override
    public Flowable<List<Category>> getCategories(@NonNull Integer restaurantId, @NonNull Integer serviceId) {
        return remoteRepository.getCategories(restaurantId, serviceId);
    }

    @Override
    public Single<Category> getCategory(@NonNull Integer restaurantId, @NonNull Integer serviceId, @NonNull Integer categoryId) {
        return remoteRepository.getCategory(restaurantId, serviceId, categoryId);
    }

    @Override
    public Flowable<List<Institute>> getInstitutions() {
        // Respond immediately with cache if available and not valid
        if (institutions != null && !institutionsAreValid) {
            return Flowable.fromIterable(institutions.values()).toList().toFlowable();
        } else if (institutions == null)
            institutions = new LinkedHashMap<>();

        Flowable<List<Institute>> remoteList = getAndSaveRemoteInstitutions();

        if (institutionsAreValid)
            return remoteList;
        else {
            // Query the local storage if available. If not, query the network.
            Flowable<List<Institute>> localList = getAndCacheLocalInstitutions();
            return Flowable.concat(localList, remoteList)
                    .filter(data -> !data.isEmpty())
                    .firstOrError()
                    .toFlowable();
        }
    }

    private Flowable<List<Institute>> getAndCacheLocalInstitutions() {
        return localRepository.getInstitutions()
                .flatMap(data -> Flowable.fromIterable(data)
                        .doOnNext(institute -> institutions.put(institute.getId(), institute))
                        .toList()
                        .toFlowable());
    }

    private Flowable<List<Institute>> getAndSaveRemoteInstitutions() {
        return remoteRepository.getInstitutions()
                .flatMap(data -> Flowable.fromIterable(data)
                        .doOnNext(institute -> {
                            localRepository.saveInstitution(institute);
                            institutions.put(institute.getId(), institute);
                        })
                        .toList()
                        .toFlowable())
                .doOnComplete(() -> institutionsAreValid = false);
    }


    @Override
    public Flowable<List<Cusine>> getCusines() {
        // Respond immediately with cache if available and not valid
        if (cusines != null && !cusinesAreValid) {
            return Flowable.fromIterable(cusines.values()).toList().toFlowable();
        } else if (cusines == null)
            cusines = new LinkedHashMap<>();

        Flowable<List<Cusine>> remoteList = getAndSaveRemoteCusines();

        if (cusinesAreValid)
            return remoteList;
        else {
            // Query the local storage if available. If not, query the network.
            Flowable<List<Cusine>> localList = getAndCacheLocalCusines();
            return Flowable.concat(localList, remoteList)
                    .filter(data -> !data.isEmpty())
                    .firstOrError()
                    .toFlowable();
        }
    }

    private Flowable<List<Cusine>> getAndSaveRemoteCusines() {
        return remoteRepository.getCusines()
                .flatMap(data -> Flowable.fromIterable(data)
                        .doOnNext(cusine -> {
                            localRepository.saveCusine(cusine);
                            cusines.put(cusine.getId(), cusine);
                        })
                        .toList()
                        .toFlowable())
                .doOnComplete(() -> cusinesAreValid = false);
    }

    private Flowable<List<Cusine>> getAndCacheLocalCusines() {
        return localRepository.getCusines()
                .flatMap(data -> Flowable.fromIterable(data)
                        .doOnNext(cusine -> cusines.put(cusine.getId(), cusine))
                        .toList()
                        .toFlowable());
    }

    @Override
    public void refreshCusines() {
        cusinesAreValid = true;
    }

    @Override
    public void refreshInstitutions() {
        institutionsAreValid = true;

    }
}
