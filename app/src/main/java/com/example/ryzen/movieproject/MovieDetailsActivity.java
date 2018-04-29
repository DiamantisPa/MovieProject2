package com.example.ryzen.movieproject;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private static final int TRAILER_INT = 0;
    private static final int REVIEW_INT = 1;

    private Loader<List<Video>> mVideoLoader;
    private Loader<List<Reviews>> mReviewLoader;
    private String trailerYoutubeUrl = "http://www.youtube.com/watch?v=";
    private String appIntentTrailer = "vnd.youtube:";
    private String trailerKey;
    private String movieId;
    private String movieTitle;
    private String trailerImage;
    private String author;
    private String content;
    private String urlForPoster;
    private String movieReleaseDate;
    private String movieVoteAverage;
    private String movieOverview;
    private String TRAILER_SITE = "YouTube";
    private ImageView trailerThumbnail;
    private ImageView youtubeView;
    private TextView content1TextView;
    private TextView author1TextView;
    private ScrollView scrollView;
    private Button favouriteButton;
    private MovieDbHelper mMovieDpHelper;
    private int stateOfHeartButton = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        youtubeView = (ImageView) findViewById(R.id.youtube_button);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        ImageView imageView = (ImageView) findViewById(R.id.movie_image);
        TextView titleTextView = (TextView) findViewById(R.id.movie_title);
        TextView releaseDateTextView = (TextView) findViewById(R.id.movie_release_date);
        TextView voteAverageTextView = (TextView) findViewById(R.id.movie_vote_average);
        TextView overViewTextView = (TextView) findViewById(R.id.movie_overview);
        favouriteButton = (Button) findViewById(R.id.heart_button);
        content1TextView = (TextView) findViewById(R.id.content1);
        author1TextView = (TextView) findViewById(R.id.author1);
        trailerThumbnail = (ImageView) findViewById(R.id.movie_trailer);
        trailerThumbnail.setAdjustViewBounds(true);
        trailerThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        trailerThumbnail.setClickable(true);
        trailerThumbnail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view) {
                Intent trailerAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appIntentTrailer + trailerKey));
                Intent trailerWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerYoutubeUrl + trailerKey));

                try {
                    startActivity(trailerAppIntent);
                }catch (ActivityNotFoundException e) {
                    startActivity(trailerWebIntent);
                }
            }
        });

        Movies currentMovie = getIntent().getExtras().getParcelable(Movies.class.getSimpleName());
        urlForPoster = currentMovie.getImagePath();
        movieId = currentMovie.getId();
        movieTitle = currentMovie.getTitle();
        movieReleaseDate = currentMovie.getReleaseDate();
        movieVoteAverage = currentMovie.getVoteAverage();
        movieOverview = currentMovie.getOverview();
        String posterPath = MovieData.imageUrl(urlForPoster);
        mMovieDpHelper = new MovieDbHelper(this);
        getSupportLoaderManager().initLoader(TRAILER_INT, null, this);
        getSupportLoaderManager().initLoader(REVIEW_INT, null, this);
        checkFavouriteMovie(movieId);
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteButton(stateOfHeartButton, movieId, movieTitle, movieReleaseDate, movieOverview,
                                urlForPoster, trailerImage, movieVoteAverage, author, content);
            }
        });

        Picasso.with(this)
                .load(posterPath)
                .into(imageView);

        titleTextView.setText(movieTitle);
        releaseDateTextView.setText(movieReleaseDate);
        voteAverageTextView.setText(movieVoteAverage);
        overViewTextView.setText(movieOverview);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String path;
        path = movieId;

        switch (id) {
            case (TRAILER_INT) :
                mVideoLoader = new TrailerLoader(this, path);
                return mVideoLoader;

            case (REVIEW_INT) :
                mReviewLoader = new ReviewLoader(this,path);
                return mReviewLoader;

            default :
                break;
        }
            return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        String trailerSite;
        int id = loader.getId();

        if (data != null ) {

            switch (id) {
                case TRAILER_INT:
                    List<Video> videoList = (List<Video>) data;
                    if (videoList.size() != 0) {
                        for (int i = 0; i < videoList.size(); i++) {
                            trailerSite = videoList.get(i).getSite();
                            trailerKey = videoList.get(i).getKey();

                            if (trailerSite.equals(TRAILER_SITE)) {
                                trailerImage = VideoData.trailerThumbnail(trailerKey);
                                Picasso.with(this)
                                        .load(trailerImage)
                                        .into(trailerThumbnail);
                                youtubeView.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                        break;
                    }
                case REVIEW_INT:
                    List<Reviews> reviewsList = (List<Reviews>) data;
                    if (reviewsList.size() != 0 ) {
                        content = reviewsList.get(0).getContent();
                        author = reviewsList.get(0).getAuthor();

                        content1TextView.setText(content);
                        author1TextView.setText(author);
                        break;
                    }
            }

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        loader = null;
    }

    private void checkFavouriteMovie (String id) {
        SQLiteDatabase sqLiteDatabase = mMovieDpHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MovieContract.MovieEntry.TABLE_NAME + " WHERE movie_id = '"
        + id + "'", null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            favouriteButton.setBackgroundResource(R.drawable.ic_favourite_button);
            stateOfHeartButton = 1;
            cursor.close();
        } else if (cursor.getCount() == 0) {
            favouriteButton.setBackgroundResource(R.drawable.ic_heart_button);
            stateOfHeartButton = 0;
            cursor.close();
        }

    }

    private void favouriteButton (int state,String id, String title , String releaseDate, String overview, String path,
                                  String imageTrailer, String vote, String writer, String review) {
        switch (state) {
            case 0 :
                Uri movieInsertUri = MovieContract.MovieEntry.CONTENT_URI;
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE, releaseDate);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW , overview);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH, path);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER, imageTrailer);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, vote);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_AUTHOR, writer);
                values.put(MovieContract.MovieEntry.COLUMN_MOVIE_REVIEW, review);
                getContentResolver().insert(movieInsertUri, values);
                favouriteButton.setBackgroundResource(R.drawable.ic_favourite_button);
                stateOfHeartButton = 1;
                break;

            case 1 :
                Uri movieDeleteUri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(id).build();
                getContentResolver().delete(movieDeleteUri,null,null);
                favouriteButton.setBackgroundResource(R.drawable.ic_heart_button);
                stateOfHeartButton = 0;
                break;
        }
    }
}

