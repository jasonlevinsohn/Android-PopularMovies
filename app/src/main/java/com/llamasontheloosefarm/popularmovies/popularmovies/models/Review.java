package com.llamasontheloosefarm.popularmovies.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Review implements Parcelable {

    private String reviewId;
    private String author;
    private String content;
    private String url;

    public Review(String reviewId, String author, String content, String url) {
        this.reviewId = reviewId;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getReviewId() { return reviewId; }

    public String getAuthor() { return author; }

    public String getContent() { return content; }

    public String getUrl() { return url; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.reviewId);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }

    protected Review(Parcel in) {
        this.reviewId = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };
}
