package com.example.movieapp.controller;

public class FavoriteRequest {
    private String media_type;
    private int media_id;
    private boolean favorite;

    public FavoriteRequest(String media_type, int media_id, boolean favorite) {
        this.media_type = media_type;
        this.media_id = media_id;
        this.favorite = favorite;
    }

    public String getMedia_type() {
        return media_type;
    }

    public int getMedia_id() {
        return media_id;
    }

    public boolean isFavorite() {
        return favorite;
    }
}

