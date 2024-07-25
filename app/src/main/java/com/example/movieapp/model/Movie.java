package com.example.movieapp.model;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("title")
    private String title;
    @SerializedName("id")
    private int id;
    @SerializedName("backdrop_path")
    private String backdrop_path;

    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("overview")
    private String overview;

    @SerializedName("name")
    private String name;

    public Movie(String title, int id, String backdrop_path, String posterPath, String overview, String name) {
        this.title = title;
        this.id = id;
        this.backdrop_path = backdrop_path;
        this.posterPath = posterPath;
        this.overview = overview;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public int getId() {
        return id;
    }
}
