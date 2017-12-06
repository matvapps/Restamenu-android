package com.restamenu.api;

import android.support.annotation.NonNull;

import com.restamenu.api.DataSource.GetRestaurantCallback;
import com.restamenu.api.DataSource.LoadRestaurantsCallback;
import com.restamenu.api.service.RestaurantService;
import com.restamenu.model.content.Category;
import com.restamenu.model.content.Restaurant;
import com.restamenu.model.responce.CategoriesResponce;
import com.restamenu.model.responce.InstitutionsResponse;
import com.restamenu.model.responce.RestaurantsResponce;
import com.restamenu.util.AppExecutors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author Roodie
 */

public class RemoteRepository {

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;
    private final RestaurantService restaMenuService;
    private final AppExecutors executors;

    public RemoteRepository(AppExecutors executors, RestaurantService restaMenuService) {
        this.executors = executors;
        this.restaMenuService = restaMenuService;
    }

    public void getRestaurants(@NonNull final Integer cityId, final @NonNull LoadRestaurantsCallback callback) {
        getRestaurants(cityId, null, null, null, null, true, callback);
    }

    public void getRestaurants(@NonNull final Integer cityId, final String keyword, final Integer cusineId, final Integer instituteId, final Integer page, final boolean details, @NonNull final LoadRestaurantsCallback callback) {
        executors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<RestaurantsResponce> call = restaMenuService.getRestaurants(cityId, keyword, cusineId, instituteId, page, details);
                call.enqueue(new Callback<RestaurantsResponce>() {
                    @Override
                    public void onResponse(Call<RestaurantsResponce> call, Response<RestaurantsResponce> response) {
                        if (response.code() == 200) {
                            callback.onNext(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantsResponce> call, Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        });
    }

    public void getInstitutions(@NonNull final DataSource.GetInstitutionsCallback callback) {
        executors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<InstitutionsResponse> call = restaMenuService.getInstitutions();
                call.enqueue(new Callback<InstitutionsResponse>() {
                    @Override
                    public void onResponse(Call<InstitutionsResponse> call, Response<InstitutionsResponse> response) {
                        if(response.code() == 200) {
                            callback.onNext(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<InstitutionsResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    public void getNearRestaurants(@NonNull final Integer cityId, final String geo, final boolean details, final @NonNull LoadRestaurantsCallback callback) {
        executors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<RestaurantsResponce> call = restaMenuService.getNearRestaurants(cityId, geo, details);
                call.enqueue(new Callback<RestaurantsResponce>() {
                    @Override
                    public void onResponse(Call<RestaurantsResponce> call, Response<RestaurantsResponce> response) {
                        if (response.code() == 200) {
                            callback.onNext(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantsResponce> call, Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        });
    }

    public void getRestaurant(final @NonNull Integer id, final @NonNull GetRestaurantCallback callback) {
        executors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<Restaurant> call = restaMenuService.getRestaurant(id, null);
                call.enqueue(new Callback<Restaurant>() {
                    @Override
                    public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                        if (response.code() == 200) {
                            callback.onNext(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Restaurant> call, Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        });
    }

    public void getCategories(@NonNull final Integer restId, final Integer serviceId, final @NonNull DataSource.GetCategoriesCallback callback) {
        executors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<CategoriesResponce> call = restaMenuService.getCategories(restId, serviceId, null);
                call.enqueue(new Callback<CategoriesResponce>() {
                    @Override
                    public void onResponse(Call<CategoriesResponce> call, Response<CategoriesResponce> response) {
                        if (response.code() == 200) {
                            callback.onNext(response.body().getCategories());
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoriesResponce> call, Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        });
    }

    public void getCategory(@NonNull final Integer restId, final Integer serviceId, final Integer categoryId, final @NonNull DataSource.GetCategoryCallback callback) {
        executors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<Category> call = restaMenuService.getCategory(restId, serviceId, categoryId, null, null);
                call.enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(Call<Category> call, Response<Category> response) {
                        if (response.code() == 200) {
                            callback.onNext(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Category> call, Throwable t) {
                        callback.onError(t);
                    }
                });
            }
        });
    }
}
