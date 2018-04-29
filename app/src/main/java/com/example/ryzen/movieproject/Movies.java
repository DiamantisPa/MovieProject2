package com.example.ryzen.movieproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ryzen on 3/12/2018.
 */

public class Movies implements Parcelable{

    private String id;
    private String title;
    private String voteAverage;
    private String imagePath;
    private String overview;
    private String releaseDate;


    public Movies(String id, String title, String voteAverage,
                  String imagePath, String overview, String releaseDate) {

        this.id = id;
        this.title = title;
        this.voteAverage = voteAverage;
        this.imagePath = imagePath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel parcel) {
            return new Movies(parcel);
        }

        @Override
        public Movies[] newArray(int i) {
            return new Movies[i];
        }
    };

    public Movies(Parcel parcel) {

        this.id = parcel.readString();
        this.title = parcel.readString();
        this.voteAverage = parcel.readString();
        this.imagePath = parcel.readString();
        this.overview = parcel.readString();
        this.releaseDate = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.voteAverage);
        parcel.writeString(this.imagePath);
        parcel.writeString(this.overview);
        parcel.writeString(this.releaseDate);

    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", voteAverage='" + voteAverage + '\'' +
                ", posterPath='" + imagePath + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

}

