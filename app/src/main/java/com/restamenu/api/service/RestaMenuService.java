package com.restamenu.api.service;

import android.support.annotation.NonNull;

import com.restamenu.model.content.Restaurant;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Roodie
 */

public interface RestaMenuService {

    @NonNull
    @GET("profile")
    Call<ResponseBody> getProfile();

    @NonNull
    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

}
