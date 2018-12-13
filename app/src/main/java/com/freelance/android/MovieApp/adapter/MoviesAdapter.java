package com.freelance.android.MovieApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.freelance.android.MovieApp.activities.DetailActivity;
import com.freelance.android.MovieApp.R;
import com.freelance.android.MovieApp.model.Movie;

import java.util.List;

/**
 * Created by Kyaw Khine on 09/26/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private static final String LOG_TAG = MoviesAdapter.class.getName();

    private Context mContext;
    private List<Movie> movieList;

    public MoviesAdapter(Context mContext, List<Movie> movieList) {
        Log.i(LOG_TAG, "TEST: MoviesAdapter() called...");

        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(LOG_TAG, "TEST: onCreateViewHolder() called...");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MyViewHolder holder, int position) {
        Log.i(LOG_TAG, "TEST: onBindViewHolder() called...");

        holder.title.setText(movieList.get(position).getOriginalTitle());
        String vote = Double.toString(movieList.get(position).getVoteAverage());
        holder.userrating.setText(vote);

        String poster = "https://image.tmdb.org/t/p/w500" + movieList.get(position).getPosterPath();

        Glide.with(mContext)
//                .load(movieList.get(position).getPosterPath())
                .load(poster)
                .placeholder(R.drawable.load)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        Log.i(LOG_TAG, "TEST: getItemCount() called...");

        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final String LOG_TAG = MyViewHolder.class.getName();

        public TextView title, userrating;
        public ImageView thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            Log.i(LOG_TAG, "TEST: MoviesAdapter.MyViewHolder() called...");

            title = (TextView) itemView.findViewById(R.id.title); // for movie_card.xml
            userrating = (TextView) itemView.findViewById(R.id.userRating);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG, "TEST: MoviesAdapter.MyViewHolder onClick() called...");

                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {

                        Movie clickedDataItem = movieList.get(pos);

                        Intent i = new Intent(mContext, DetailActivity.class);
                        i.putExtra("id", movieList.get(pos).getId());//Added on this day October 27th, 2017.
                        i.putExtra("original_title", movieList.get(pos).getOriginalTitle()); //I got my error for typing.
                        i.putExtra("poster_path", movieList.get(pos).getPosterPath());
                        i.putExtra("overview", movieList.get(pos).getOverview());
                        i.putExtra("vote_average", Double.toString(movieList.get(pos).getVoteAverage()));
                        i.putExtra("release_date", movieList.get(pos).getReleaseDate());
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);

                        Toast.makeText(view.getContext(), "You clicked " + clickedDataItem.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
