package com.example.ryzen.movieproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzen on 4/27/2018.
 */

public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.MovieCursorAdapterViewHolder> {

    private final Context mContext;
    private final MovieCursorAdapterOnClickHandler movieCursorAdapterOnClickHandler;
    private Cursor movieCursor;
    private List<Movies> moviesList = new ArrayList<>();

    public interface MovieCursorAdapterOnClickHandler {
            void onClick (int position);
    }

    public MovieCursorAdapter (@NonNull Context context, MovieCursorAdapterOnClickHandler clickHandler) {
        mContext = context;
        movieCursorAdapterOnClickHandler = clickHandler;

    }

    @Override
    public MovieCursorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, parent , false);

        return new MovieCursorAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieCursorAdapterViewHolder holder, int position) {
        movieCursor.moveToPosition(position);

        String posterPath = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTERPATH));
        String id = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
        String title = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
        String voteAverage = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE));
        String overview = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
        String date =   movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_DATE));

        Movies movie = new Movies(id , title, voteAverage, posterPath,overview,date);
        moviesList.add(movie);

            Picasso.with(mContext)
                    .load(MovieData.imageUrl(posterPath))
                    .into(holder.posterImage);

    }

    @Override
    public int getItemCount() {
       if (null == movieCursor  ) return 0;
       return movieCursor.getCount();
    }

    void swapCursor (Cursor newCursor) {
        movieCursor = newCursor;
        notifyDataSetChanged();
    }

    class MovieCursorAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView posterImage;

        MovieCursorAdapterViewHolder (View view) {
            super(view);

            posterImage =(ImageView) view.findViewById(R.id.movie_list_image);

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Intent intent = new Intent(mContext, MovieDetailsActivity.class);
            intent.putExtra(Movies.class.getSimpleName(), moviesList .get(adapterPosition));
            mContext.startActivity(intent);
        }
    }
}
