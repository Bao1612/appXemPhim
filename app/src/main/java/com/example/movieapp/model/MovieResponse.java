package com.example.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName("results")
    private List<Film> results;

    public List<Film> getResults() {
        return results;
    }
}


