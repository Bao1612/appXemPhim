package com.example.movieapp.api;

import com.example.movieapp.model.FavoriteRequest;
import com.example.movieapp.model.MovieResponse;

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
    Call<Void> markAsFavorite(
            @Path("account_id") String accountId,
            @Query("api_key") String apiKey,
            @Body FavoriteRequest favoriteRequest
    );

    @GET("{account_id}/favorite/movies")
    Call<MovieResponse> getFavouriteMovie(@Path("account_id") String accountId);

}
