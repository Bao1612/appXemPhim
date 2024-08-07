package com.example.movieapp.ui;

import static com.example.movieapp.util.GeneralUtil.account_id;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.api.ApiClient;
import com.example.movieapp.api.ApiService;
import com.example.movieapp.controller.MovieAdapter;
import com.example.movieapp.controller.OnMovieClickListener;
import com.example.movieapp.databinding.FragmentFavouriteBinding;
import com.example.movieapp.databinding.FragmentMainBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavouriteFragment extends Fragment implements OnMovieClickListener {

    private FragmentFavouriteBinding binding;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private ApiService apiService;
    private String[] type = {"Movie", "Tv"};
    private ArrayAdapter<String> adapterItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        apiService = ApiClient.getRetrofitClient().create(ApiService.class);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.rcvMovie.setLayoutManager(gridLayoutManager);
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(requireContext(), movieList, this);
        binding.rcvMovie.setAdapter(adapter);

        getMovieData();
        chooseMovieType();

        return view;
    }

    private void chooseMovieType() {
        adapterItems = new ArrayAdapter<>(requireContext(), R.layout.list_item, type);
        binding.movieType.setAdapter(adapterItems);
        binding.movieType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Movie")) {
                    getMovieData();
                } else if (parent.getItemAtPosition(position).toString().equals("Tv")) {
                    getTvData();
                }
            }
        });
    }

    private void getMovieData() {
        // Call the getMovies method without the Authorization header
        Call<MovieResponse> call = apiService.getFavouriteMovie(account_id);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movieList.clear();
                adapter.notifyDataSetChanged();
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

    private void getTvData() {
        // Call the getMovies method without the Authorization header
        Call<MovieResponse> call = apiService.getFavouriteTv(account_id);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movieList.clear();
                adapter.notifyDataSetChanged();
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

    @Override
    public void onMovieClick(Movie movie) {

    }
}