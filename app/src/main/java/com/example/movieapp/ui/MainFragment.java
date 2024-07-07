package com.example.movieapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movieapp.api.ApiClient;
import com.example.movieapp.api.ApiService;
import com.example.movieapp.controller.MovieAdapter;
import com.example.movieapp.databinding.FragmentMainBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize RecyclerView and Adapter

        apiService = ApiClient.getRetrofitClient().create(ApiService.class);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.rcvMovie.setLayoutManager(gridLayoutManager);
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(requireContext(), movieList);
        binding.rcvMovie.setAdapter(adapter);

        // Load movie data
        getMovieData();
//        searchMovie();
        return view;
    }

    private void getMovieData() {
        // Call the getMovies method without the Authorization header
        Call<MovieResponse> call = apiService.getMovies(1);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    movieList.addAll(movies);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Call API error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchMovie() {
        binding.searchMovie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) { // Ensure s is not null and not empty
                    Call<MovieResponse> searchMovie = apiService.searchMovie(s.toString());
                    searchMovie.enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Movie> movies = response.body().getResults();
                                movieList.clear(); // Clear the existing list
                                movieList.addAll(movies);// Add new movies

                                for (Movie movie : movies) {
                                    Log.d("BAO_DEBUG", "name: " + movie.getTitle());
                                    Log.d("BAO_DEBUG", "path: " + movie.getPosterPath());
                                }

                                adapter.notifyDataSetChanged(); // Notify adapter of data changes
                            } else {
                                Toast.makeText(requireContext(), "Call API error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                            Toast.makeText(requireContext(), "API call failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    movieList.clear(); // Clear the list if the search text is empty
                    adapter.notifyDataSetChanged(); // Notify adapter of data changes
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });
    }


}