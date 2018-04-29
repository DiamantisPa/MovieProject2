package com.example.ryzen.movieproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ryzen on 4/25/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.dp";

    public static final int DATABASE_INT = 1;

    public MovieDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_INT);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "      +

                        MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "      +

                        MovieContract.MovieEntry.COLUMN_MOVIE_DATE + " INTEGER NOT NULL, "       +

                        MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "   +

                        MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH + " TEXT NOT NULL, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER   + " TEXT, "           +

                        MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " INTEGER NOT NULL, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_REVIEW       + " TEXT, "                          +

                        MovieContract.MovieEntry.COLUMN_MOVIE_AUTHOR +          " TEXT );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
