package com.example.ryzen.movieproject;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ryzen on 4/25/2018.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.ryzen.movieproject";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";
        public static final String COLUMN_MOVIE_POSTERPATH = "movie_posterpath";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average";
        public static final String COLUMN_MOVIE_REVIEW = "movie_review";
        public static final String COLUMN_MOVIE_AUTHOR = "movie_author";
        public static final String COLUMN_MOVIE_TRAILER = "movie_trailer";
        public static final String COLUMN_MOVIE_DATE = "movie_release_date";

    }
}
