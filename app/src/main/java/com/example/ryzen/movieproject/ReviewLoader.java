package com.example.ryzen.movieproject;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzen on 4/23/2018.
 */

public class ReviewLoader extends AsyncTaskLoader<List<Reviews>> {

    String mUrl;

    public ReviewLoader (Context context, String path) {
        super(context);

        mUrl = path;
    }

    @Override
    protected void onStartLoading() {forceLoad();}

    @Override
    public List<Reviews> loadInBackground() {

        List<Reviews> reviews = new ArrayList<>();

        try {
            reviews = ReviewData.parseReviewsData(ReviewData.buildReviewUrl(mUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
