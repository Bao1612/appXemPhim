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
import com.example.movieapp.controller.MovieAdapter;
import com.example.movieapp.controller.OnMovieClickListener;
import com.example.movieapp.databinding.FragmentMainBinding;
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


public class MainFragment extends Fragment implements OnMovieClickListener {

    private FragmentMainBinding binding;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private ApiService apiService;
    private GeneralUtil generalUtil;
    private int page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize RecyclerView and Adapter

        apiService = ApiClient.getRetrofitClient().create(ApiService.class);
        page = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.rcvMovie.setLayoutManager(gridLayoutManager);
        movieList = new ArrayList<>();
        generalUtil = new GeneralUtil();
        adapter = new MovieAdapter(requireContext(), movieList, this);
        binding.rcvMovie.setAdapter(adapter);

        // Load movie data
        getMovieData(page);
        prevPage();
        nextPage();
//        searchMovie();
        return view;
    }

    private void getMovieData(int page) {
        // Call the getMovies method without the Authorization header
        Call<MovieResponse> call = apiService.getMovies(page);
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

    private void prevPage() {
        binding.prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prevPageNumber = String.valueOf(Integer.parseInt(binding.pageNumber.getText().toString()) - 1);

                if(prevPageNumber.equals("0")) {
                    binding.pageNumber.setText("1");
                } else {
                    binding.pageNumber.setText(prevPageNumber);
                    getMovieData(Integer.parseInt(prevPageNumber));
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
                getMovieData(Integer.parseInt(nextPageNumber));
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

        movieName.setText(movie.getTitle());
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
                generalUtil.addFavoriteTvSeries("movie", movie.getId());
                Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show();

            }
        });

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) viewDialog.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }


}