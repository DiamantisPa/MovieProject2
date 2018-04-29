package com.example.ryzen.movieproject;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzen on 4/20/2018.
 */

public class TrailerLoader extends android.support.v4.content.AsyncTaskLoader<List<Video>> {

    String mUrl;

    public TrailerLoader (Context context, String path) {
        super(context);

        mUrl = path;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Video> loadInBackground() {

        List<Video> list = new ArrayList<>();

        try {
            list = VideoData.parseTrailerData(VideoData.buildVideoUlr(mUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
