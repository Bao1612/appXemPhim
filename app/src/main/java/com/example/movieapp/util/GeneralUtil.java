package com.example.movieapp.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.movieapp.api.ApiClient;
import com.example.movieapp.api.ApiService;
import com.example.movieapp.controller.FavoriteResponse;
import com.example.movieapp.model.FavoriteRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralUtil {
    public static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlODc2ZjE2YWJlODc4NTM3MzVmNzk2ZTQ2YjMzMWFlZiIsIm5iZiI6MTcyMDI3MzQ4OC4yNzkwMzEsInN1YiI6IjY0MGI0MDkxY2FhY2EyMDBiODA0NmY4YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.m5yehXqli_zLDotkd-OqSBJpHCk8lKW0YlOaGzTdmS0";
    public static final int account_id = 18144727;
    private ApiService apiService;
    private Context context;

    public void addFavoriteTvSeries(String movieType, int movieId) {

        apiService = ApiClient.getRetrofitClient().create(ApiService.class);
        FavoriteRequest favoriteRequest = new FavoriteRequest(movieType, movieId, true);

        Call<FavoriteResponse> call = apiService.addFavorite(account_id, "", favoriteRequest);
        call.enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(Call<FavoriteResponse> call, Response<FavoriteResponse> response) {
                if (response.isSuccessful()) {
                    FavoriteResponse favoriteResponse = response.body();
                } else {
                    Log.d("TMDb", "Response Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<FavoriteResponse> call, Throwable t) {
                Log.d("TMDb", "Request Failed: " + t.getMessage());
            }
        });
    }

}
