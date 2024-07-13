package com.example.movieapp.api;

import android.util.Log;

import com.example.movieapp.controller.FavoriteResponse;
import com.example.movieapp.model.FavoriteRequest;
import com.example.movieapp.model.LoginResponse;
import com.example.movieapp.model.MovieResponse;
import com.example.movieapp.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("discover/movie") // Adjust the endpoint as needed
    Call<MovieResponse> getMovies(@Query("page") int page);

    @GET("discover/tv") // Adjust the endpoint as needed
    Call<MovieResponse> getTvSeries(@Query("page") int page);

    @GET("search/movie")
    Call<MovieResponse> searchMovie(@Query("movieName") String movieName);

    @POST("account/{account_id}/favorite")
    Call<FavoriteResponse> addFavorite(
            @Path("account_id") int accountId,
            @Query("session_id") String sessionId,
            @Body FavoriteRequest favoriteRequest
    );

    @GET("account/{account_id}/favorite/movies")
    Call<MovieResponse> getFavouriteMovie(@Path("account_id") int accountId);

    @GET("account/{account_id}/favorite/tv")
    Call<MovieResponse> getFavouriteTv(@Path("account_id") int accountId);

    @POST("auth/login")
    Call<LoginResponse> getLogin(@Body User user);
}
