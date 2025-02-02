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
import com.example.movieapp.controller.TvSeriesController;
import com.example.movieapp.util.OnMovieClickListener;
import com.example.movieapp.adapter.TvSeriesAdapter;
import com.example.movieapp.databinding.FragmentTvBinding;
import com.example.movieapp.model.Film;
import com.example.movieapp.util.GeneralUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TvSeriesFragment extends Fragment implements OnMovieClickListener {

    private FragmentTvBinding binding;
    private TvSeriesAdapter adapter;
    private List<Film> tvSeriesList;
    private GeneralUtil generalUtil;
    private TvSeriesController tvSeriesController;
    private int page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTvBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.rcvMovie.setLayoutManager(gridLayoutManager);
        tvSeriesList = new ArrayList<>();
        page = 1;
        tvSeriesController = new TvSeriesController();
        adapter = new TvSeriesAdapter(requireContext(), tvSeriesList, this);
        binding.rcvMovie.setAdapter(adapter);
        generalUtil = new GeneralUtil();
        getTvSeriesData(page);
        nextPage();
        prevPage();


        return view;
    }

    private void getTvSeriesData(int page) {
        tvSeriesController.fetchTvs(page, new TvSeriesController.TvCallback() {
            @Override
            public void onSuccess(List<Film> TvSeries) {
                tvSeriesList.clear();
                tvSeriesList.addAll(TvSeries);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {

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
                    getTvSeriesData(Integer.parseInt(prevPageNumber));
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
                getTvSeriesData(Integer.parseInt(nextPageNumber));
            }
        });
    }

    @Override
    public void onMovieClick(Film movie) {
        clickBottomSheetDialog(movie);
    }

    private void clickBottomSheetDialog(Film movie) {
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
                Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show();

            }
        });

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) viewDialog.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

}