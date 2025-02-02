package com.example.movieapp.controller;

import com.example.movieapp.api.ApiClient;
import com.example.movieapp.api.ApiService;
import com.example.movieapp.model.Film;
import com.example.movieapp.model.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieController {
    private ApiService apiService;

    public interface MovieCallback {
        void onSuccess(List<Film> movies);

        void onError(String errorMessage);
    }

    public void fetchMovies(int page, MovieCallback callback) {
        apiService = ApiClient.getRetrofitClient().create(ApiService.class);
        apiService.getMovies(page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResults());
                } else {
                    callback.onError("Api error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {

            }
        });
    }


}
