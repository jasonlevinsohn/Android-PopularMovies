package com.llamasontheloosefarm.popularmovies.popularmovies.models;

public class Movie {

    private String title;
    private String posterImage;

    public Movie(String title, String posterImage) {
        this.title = title;
        this.posterImage = posterImage;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPosterImage() {
        return this.posterImage;
    }
}
