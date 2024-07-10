package com.example.movieapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movieapp.api.ApiClient;
import com.example.movieapp.api.ApiService;
import com.example.movieapp.controller.MovieAdapter;
import com.example.movieapp.controller.OnMovieClickListener;
import com.example.movieapp.databinding.FragmentMainBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment implements OnMovieClickListener {

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
        adapter = new MovieAdapter(requireContext(), movieList, this);
        binding.rcvMovie.setAdapter(adapter);

        // Load movie data
        getMovieData();
        prevPage();
        nextPage();
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

    private void prevPage() {
        binding.prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prevPageNumber = String.valueOf(Integer.parseInt(binding.pageNumber.getText().toString()) - 1);

                if(prevPageNumber.equals("0")) {
                    binding.pageNumber.setText("1");
                } else {
                    binding.pageNumber.setText(prevPageNumber);
                }

            }
        });
    }

    private void nextPage() {
        binding.nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nextPageNumber = String.valueOf(Integer.parseInt(binding.pageNumber.getText().toString()) + 1);

                binding.pageNumber.setText(nextPageNumber);

            }
        });
    }


    @Override
    public void onMovieClick(Movie movie) {
        Log.d("BAO_DEBUG", "title: " + movie.getTitle());
    }
}