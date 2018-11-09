package com.llamasontheloosefarm.popularmovies.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String movieId;
    private String title;
    private String posterImage;
    private String releaseDate;
    private String voteAverage;
    private String plot;

    public Movie(String movieId, String title, String posterImage, String releaseDate, String voteAverage, String plot) {
        this.movieId = movieId;
        this.title = title;
        this.posterImage = posterImage;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plot = plot;
    }

    public String getMovieId() { return this.movieId; }

    public String getTitle() {
        return this.title;
    }

    public String getPosterImage() {
        return this.posterImage;
    }

    public String getReleaseDate() { return this.releaseDate; }

    public String getVoteAverage() { return this.voteAverage; }

    public String getPlot() { return this.plot; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieId);
        dest.writeString(this.title);
        dest.writeString(this.posterImage);
        dest.writeString(this.releaseDate);
        dest.writeString(this.voteAverage);
        dest.writeString(this.plot);
    }

    protected Movie(Parcel in) {
        this.movieId = in.readString();
        this.title = in.readString();
        this.posterImage = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readString();
        this.plot = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
