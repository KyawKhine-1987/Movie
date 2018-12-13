package com.freelance.android.MovieApp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.freelance.android.MovieApp.BuildConfig;
import com.freelance.android.MovieApp.R;
import com.freelance.android.MovieApp.adapter.TrailerAdapter;
import com.freelance.android.MovieApp.api.Client;
import com.freelance.android.MovieApp.api.Service;
import com.freelance.android.MovieApp.data.FavoriteDbHelper;
import com.freelance.android.MovieApp.model.Movie;
import com.freelance.android.MovieApp.model.Trailer;
import com.freelance.android.MovieApp.model.TrailerResponse;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kyaw Khine on 09/26/2017.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getName();

    TextView nameOfMovie, plotSynopsis, userRating, releaseDate;
    ImageView imageView;

    private RecyclerView recyclerView;
    private TrailerAdapter adapter;
    private List<Trailer> trailerList;
    private FavoriteDbHelper favoriteDbHelper;
    private Movie favorite;
    private final AppCompatActivity activity = DetailActivity.this;
//    public int movie_id = getIntent().getExtras().getInt("id");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: onCreate() called...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar tB = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(tB);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        imageView = (ImageView) this.findViewById(R.id.thumbnail_image_header);
        nameOfMovie = (TextView) this.findViewById(R.id.title);
        plotSynopsis = (TextView) this.findViewById(R.id.plotSynopsis);
        userRating = (TextView) this.findViewById(R.id.userRating);
        releaseDate = (TextView) this.findViewById(R.id.releaseDate);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra("original_title")) {

            String thumbnail = getIntent().getExtras().getString("poster_path");
            String movieName = getIntent().getExtras().getString("original_title");
            String synopsis = getIntent().getExtras().getString("overview");
            String rating = getIntent().getExtras().getString("vote_average");
            String dateOfRelease = getIntent().getExtras().getString("release_date");

            String poster = "https://image.tmdb.org/t/p/w500" + thumbnail;

            Glide.with(this)
                    //.load(thumbnail)
                    .load(poster)
                    .placeholder(R.drawable.load)
                    .into(imageView);
            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
        } else {
            Toast.makeText(this, "No API Data!", Toast.LENGTH_SHORT).show();
        }

        MaterialFavoriteButton mFBNice = (MaterialFavoriteButton) findViewById(R.id.favorite_button);
        SharedPreferences sP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mFBNice.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                Log.i(LOG_TAG, "TEST: onFavoriteChanged() called...");

                if (favorite) {
                    SharedPreferences.Editor editor = getSharedPreferences("com.freelance.android.MovieApp.activities.DetailActivity", MODE_PRIVATE).edit();
                    editor.putBoolean("Favorite Added", true);//Error Typing
                    editor.commit();
                    saveFavorite();
                    Snackbar.make(buttonView, "Added to Favorite.", Snackbar.LENGTH_SHORT).show();
                } else {
                    int movie_id = getIntent().getExtras().getInt("id");
                    favoriteDbHelper = new FavoriteDbHelper(DetailActivity.this);
                    favoriteDbHelper.deleteFavorite(movie_id);

                    SharedPreferences.Editor editor = getSharedPreferences("com.freelance.android.MovieApp.activities.DetailActivity", MODE_PRIVATE).edit();
                    editor.putBoolean("Favorite Remove", true);
                    editor.commit();
                    Snackbar.make(buttonView, "Removed to Favorite.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        initViews();
    }

    private void saveFavorite() {
        Log.i(LOG_TAG, "TEST: saveFavorite() called...");

        favoriteDbHelper = new FavoriteDbHelper(activity);
        favorite = new Movie();
        int movie_id = getIntent().getExtras().getInt("id");
        String rate = getIntent().getExtras().getString("vote_average");
        String poster = getIntent().getExtras().getString("poster_path");

        favorite.setId(movie_id);
        favorite.setOriginalTitle(nameOfMovie.getText().toString().trim());
        favorite.setVoteAverage(Double.parseDouble(rate));
        favorite.setPosterPath(poster);
        favorite.setOverview(plotSynopsis.getText().toString().trim());//I got my error for copy & paste.

        favoriteDbHelper.addFavorite(favorite);
    }

    private void initCollapsingToolbar() {
        Log.i(LOG_TAG, "TEST: initCollapsingToolbar() called...");

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) this.findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle("");

        AppBarLayout appBarLayout = (AppBarLayout) this.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.i(LOG_TAG, "TEST: onOffsetChanged() called...");

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
    }

    private void initViews() {
        Log.i(LOG_TAG, "TEST: initViews() called...");

        recyclerView = (RecyclerView) this.findViewById(R.id.rv_contentDetail);
        trailerList = new ArrayList<>();
        adapter = new TrailerAdapter(this, trailerList);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (getIntent().getExtras() != null) {
            int movie_id = getIntent().getExtras().getInt("id");
            loadJSON(movie_id);//Got the Error from API.
        }
    }

    private void loadJSON(int movie_id) {
        Log.i(LOG_TAG, "TEST: loadJSON() called...");

        //int movie_id = getIntent().getExtras().getInt("id");
        //int movie_id = 100;//Got Error

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain your API Key from themoviedb.org.", Toast.LENGTH_SHORT).show();
                return;
            }

            Client c = new Client();
            Service apiService = c.getClient().create(Service.class);
            Call<TrailerResponse> call = apiService.getMovieTrailer(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    Log.i(LOG_TAG, "TEST: onResponse() called...");

                    List<Trailer> trailer = response.body().getTrailerResults();
                    recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailer));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.i(LOG_TAG, "TEST: onFailure() called...");
                    Log.d("Error", t.getMessage());

                    Toast.makeText(DetailActivity.this, "Error fetching trailer data!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
