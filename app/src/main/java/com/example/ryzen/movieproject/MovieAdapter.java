package com.example.ryzen.movieproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ryzen on 3/17/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final Context mContext;
    private final MovieAdapterOnClickHandler mClickHandler;
    private List<Movies> mMovies;

    public void updateItems(List<Movies> movies) {

        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int position);
    }

    public MovieAdapter (@NonNull Context context, List<Movies> movies , MovieAdapterOnClickHandler clickHandler) {

        mContext = context;
        mMovies = movies;
        mClickHandler = clickHandler;

    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, parent , false);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder,final int position) {

        Picasso.with(mContext)
                .load(MovieData.imageUrl(mMovies.get(position).getImagePath()))
                .into(holder.movieImageView);
        }



    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView movieImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);

            movieImageView = (ImageView) itemView.findViewById(R.id.movie_list_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            Intent intent = new Intent(mContext, MovieDetailsActivity.class);
            intent.putExtra(Movies.class.getSimpleName(), mMovies.get(adapterPosition));
            mContext.startActivity(intent);
        }
    }

    public List<Movies> getmMovies (){return mMovies;}
}
