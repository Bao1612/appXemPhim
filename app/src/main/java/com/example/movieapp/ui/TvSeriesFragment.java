package com.example.movieapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.api.ApiClient;
import com.example.movieapp.api.ApiService;
import com.example.movieapp.controller.OnMovieClickListener;
import com.example.movieapp.controller.TvSeriesAdapter;
import com.example.movieapp.databinding.FragmentTvBinding;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;
import com.example.movieapp.util.GeneralUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvSeriesFragment extends Fragment implements OnMovieClickListener {

    private FragmentTvBinding binding;
    private TvSeriesAdapter adapter;
    private List<Movie> movieList;
    private GeneralUtil generalUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTvBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.rcvMovie.setLayoutManager(gridLayoutManager);
        movieList = new ArrayList<>();
        adapter = new TvSeriesAdapter(requireContext(), movieList, this);
        binding.rcvMovie.setAdapter(adapter);
        generalUtil = new GeneralUtil();
        getTvSeriesData();

        return view;
    }

    private void getTvSeriesData() {
        ApiService apiService = ApiClient.getRetrofitClient().create(ApiService.class);

        // Call the getMovies method without the Authorization header
        Call<MovieResponse> call = apiService.getTvSeries(1);
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

    @Override
    public void onMovieClick(Movie movie) {
        clickBottomSheetDialog(movie);
    }

    private void clickBottomSheetDialog(Movie movie) {
        View viewDialog = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);

        ImageView backdrop_path = viewDialog.findViewById(R.id.backdropPath);
        TextView movieName = viewDialog.findViewById(R.id.movieName);
        TextView overview = viewDialog.findViewById(R.id.overview);
        TextView closeBtn = viewDialog.findViewById(R.id.close_btn);
        ImageView addFavoriteBtn = viewDialog.findViewById(R.id.addFavoriteBtn);

        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getBackdrop_path()).into(backdrop_path);

        movieName.setText(movie.getName());
        overview.setText(movie.getOverview());

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        addFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalUtil.addFavoriteTvSeries("tv", movie.getId());
            }
        });

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) viewDialog.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

}