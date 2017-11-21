package com.restamenu.api;

import android.support.annotation.NonNull;

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

}
