package com.llamasontheloosefarm.popularmovies.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Trailer implements Parcelable {

    private String trailerId;
    private String key;
    private String site;
    private String type;
    private String name;

    public Trailer(String trailerId, String key, String site, String type, String name) {
        this.trailerId = trailerId;
        this.key = key;
        this.site = site;
        this.type = type;
        this.name = name;
    }

    public String getTrailerId() { return this.trailerId; }

    public String getKey() { return this.key; }

    public String getSite() { return this.site; }

    public String getType() { return this.type; }

    public String getName() { return this.name; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.trailerId);
        dest.writeString(this.key);
        dest.writeString(this.site);
        dest.writeString(this.type);
        dest.writeString(this.name);
    }

    protected Trailer(Parcel in) {
        this.trailerId = in.readString();
        this.key = in.readString();
        this.site = in.readString();
        this.type = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };
}
