package com.example.ryzen.movieproject;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzen on 3/19/2018.
 */

public class MoviesLoader extends AsyncTaskLoader<List<Movies>> {


    String mUrl;

    public MoviesLoader (Context context, String path) {
        super(context);

        mUrl = path;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movies> loadInBackground() {

        List<Movies> movies = new ArrayList<>();

        try {
             movies = MovieData.parseMovieData(MovieData.buildUrl(mUrl));
        } catch (IOException e){

        }

        return movies;
    }
}
