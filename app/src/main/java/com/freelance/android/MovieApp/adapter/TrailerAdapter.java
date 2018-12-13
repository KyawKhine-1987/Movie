package com.freelance.android.MovieApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.freelance.android.MovieApp.R;
import com.freelance.android.MovieApp.model.Trailer;

import java.util.List;

/**
 * Created by Kyaw Khine on 09/29/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private static final String LOG_TAG = TrailerAdapter.class.getName();

    private Context mContext;
    private List<Trailer> trailerList;

    public TrailerAdapter(Context mContext, List<Trailer> trailerList) {
        Log.i(LOG_TAG, "TEST: MoviesAdapter() constructor called...");

        this.mContext = mContext;
        this.trailerList = trailerList;
    }

    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(LOG_TAG, "TEST: onCreateViewHolder() called...");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.MyViewHolder holder, int position) {
        Log.i(LOG_TAG, "TEST: onBindViewHolder() called...");

        holder.title.setText(trailerList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        Log.i(LOG_TAG, "TEST: getItemCount() called...");

        return trailerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final String LOG_TAG = MyViewHolder.class.getName();

        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            Log.i(LOG_TAG, "TEST: TrailerAdapter.MyViewHolder() called...");

            title = (TextView) itemView.findViewById(R.id.title);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(LOG_TAG, "TEST: TrailerAdapter.MyViewHolder onClick() called...");

                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {

                        Trailer clickedDataItem = trailerList.get(pos);

                        String videoId = trailerList.get(pos).getKey();
                        //String videoId = "h6hZkvrFIj0";
                        //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                        i.putExtra("VIDEO_ID", videoId);
                        mContext.startActivity(i);
                        Toast.makeText(view.getContext(), "You clicked " + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

