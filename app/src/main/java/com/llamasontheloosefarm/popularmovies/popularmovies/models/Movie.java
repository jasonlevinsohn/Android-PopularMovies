package com.llamasontheloosefarm.popularmovies.popularmovies.models;

public class Movie {

    private String title;
    private String posterImage;
    private String releaseDate;
    private String voteAverage;
    private String plot;

    public Movie(String title, String posterImage, String releaseDate, String voteAverage, String plot) {
        this.title = title;
        this.posterImage = posterImage;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plot = plot;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPosterImage() {
        return this.posterImage;
    }

    public String getReleaseDate() { return this.releaseDate; }

    public String getVoteAverage() { return this.voteAverage; }

    public String getPlot() { return this.plot; }
}
