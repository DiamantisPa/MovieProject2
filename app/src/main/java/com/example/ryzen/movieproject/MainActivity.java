package com.example.ryzen.movieproject;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks,MovieAdapter.MovieAdapterOnClickHandler,MovieCursorAdapter.MovieCursorAdapterOnClickHandler{

    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_view_state";
    private static final String SOME_VALUE = "some_value";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String PROJECTION [] ={MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_DATE,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_AUTHOR,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_REVIEW};

    private static final int COLUMNS = 2;
    private static final int ID_FAVOURITE = 2;
    private static final int ID_TOP_RATED = 1;
    private static final int ID_POPULAR = 0;
    private int loaderID;

    Parcelable mListState;

    private ProgressBar mLoadingProgressBar;
    private TextView mNoNetworkTextView;
    private RecyclerView mMovieList;
    private MovieAdapter mMovieAdapter;
    private MovieCursorAdapter mMovieCursorAdapter;
    private MoviesLoader mMoviesLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNoNetworkTextView = (TextView) findViewById(R.id.no_internet_connection);
        mMovieList = (RecyclerView) findViewById(R.id.movie_recycler_view);
        mMovieList.setLayoutManager(new GridLayoutManager(this, COLUMNS));
        mMovieList.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, new ArrayList<Movies>(), this);
        mMovieList.setAdapter(mMovieAdapter);
        mMovieCursorAdapter = new MovieCursorAdapter(this, this);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.movie_progress_bar);
        loaderID = ID_POPULAR;

        if (connectedToNetwork()) {
            if(savedInstanceState == null) {
                getSupportLoaderManager().initLoader(loaderID, null, this);
            }else if (savedInstanceState != null) {
                loaderID = savedInstanceState.getInt(SOME_VALUE);

                if (loaderID == ID_FAVOURITE) {
                    getSupportLoaderManager().restartLoader(loaderID,null,this);
                }else {
                    getSupportLoaderManager().initLoader(loaderID,null,this);

                }

            }

        } else {
            mLoadingProgressBar.setVisibility(View.GONE);
            mNoNetworkTextView.setVisibility(View.VISIBLE);
        }
    }

    public boolean connectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {

        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                getSupportLoaderManager().restartLoader(ID_POPULAR, null, this);
                loaderID = ID_POPULAR;
                return true;
            case R.id.action_top_rated:
                getSupportLoaderManager().restartLoader(ID_TOP_RATED, null, this);
                loaderID = ID_TOP_RATED;
                return true;
            case R.id.action_favourite:
                getSupportLoaderManager().restartLoader(ID_FAVOURITE, null, this);
                loaderID = ID_FAVOURITE;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader onCreateLoader(int loaderId, Bundle args) {
        String path;
        int id = loaderId;
        Log.e("id", Integer.toString(id));
        if(id == ID_FAVOURITE) {
            mMovieList.setAdapter(mMovieCursorAdapter);
            mMovieCursorAdapter.notifyDataSetChanged();

            Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;

            return new CursorLoader(this,
                    movieQueryUri,
                    PROJECTION,
                    null,
                    null,
                    null);
        }

        switch (id) {
            case ID_TOP_RATED:
                mMovieList.setAdapter(mMovieAdapter);
                mMovieAdapter.notifyDataSetChanged();
                path = TOP_RATED;
                break;
            default:
                mMovieList.setAdapter(mMovieAdapter);
                mMovieAdapter.notifyDataSetChanged();
                path = POPULAR;
                break;
        }

        mMoviesLoader = new MoviesLoader(this, path);
        return mMoviesLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object movies) {
        int id = loader.getId();

        if (id == ID_POPULAR || id == ID_TOP_RATED) {
            mLoadingProgressBar.setVisibility(View.GONE);
            List<Movies> listMovies = (List<Movies>) movies;
            if (listMovies != null || listMovies.size() != 0) {
                mMovieAdapter.updateItems(listMovies);
            }
        }else if (id == ID_FAVOURITE) {
            Cursor movieCursor = (Cursor) movies;
            mMovieCursorAdapter.swapCursor(movieCursor);
            if (movieCursor.getCount() != 0) mLoadingProgressBar.setVisibility(View.GONE);

            if (mListState != null) {
                mMovieList.getLayoutManager().onRestoreInstanceState(mListState);
            }
        }


    }

    @Override
    public void onLoaderReset(Loader loader) {
        int id = loader.getId();

        if(id == ID_FAVOURITE) {
            mMovieCursorAdapter.swapCursor(null);
            return;
        }

        loader = null;
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        mListState = mMovieList.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mListState);
        savedInstanceState.putInt(SOME_VALUE, loaderID);
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        if(state != null)
            mListState = state.getParcelable(BUNDLE_RECYCLER_LAYOUT);
    }


}
