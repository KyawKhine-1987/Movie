package com.freelance.android.MovieApp.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.freelance.android.MovieApp.BuildConfig;
import com.freelance.android.MovieApp.R;
import com.freelance.android.MovieApp.adapter.MoviesAdapter;
import com.freelance.android.MovieApp.api.Client;
import com.freelance.android.MovieApp.api.Service;
import com.freelance.android.MovieApp.data.FavoriteDbHelper;
import com.freelance.android.MovieApp.model.Movie;
import com.freelance.android.MovieApp.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pD;
    private SwipeRefreshLayout swipeContainer;
    private FavoriteDbHelper favoriteDbHelper;
    private final AppCompatActivity activity = MainActivity.this;

    private static final String LOG_TAG_MoviesAdapter = MoviesAdapter.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: onCreate() called...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        /*swipeContainer = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "TEST: onRefresh() called...");

                initViews();
                Toast.makeText(getApplicationContext(), "Movies Refreshed!", Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    private Activity getActivity() {
        Log.i(LOG_TAG, "TEST: getActivity() called...");

        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void initViews() {
        Log.i(LOG_TAG, "TEST: initViews() called...");

        /*pD = new ProgressDialog(this);
        pD.setMessage("Fetching movies...");
        pD.setCancelable(false);
        pD.show();*/

        recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        favoriteDbHelper = new FavoriteDbHelper(activity);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "TEST: onRefresh() called...");

                initViews();
                Toast.makeText(getApplicationContext(), "Movies Refreshed!", Toast.LENGTH_SHORT).show();
            }
        });

        checkSortOrder();
    }

    private void loadJSON() {
        Log.i(LOG_TAG, "TEST: loadJSON() called...");

        try {

            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API Key firstly from themoviedb.org.", Toast.LENGTH_SHORT).show();
                //pD.dismiss();
                return;
            }

            Client c = new Client();
            Service apiService = c.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    Log.i(LOG_TAG, "TEST: onResponse() called...");

                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);

                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                    //pD.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.i(LOG_TAG, "TEST: onFailure() called...");

                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadJSON1() {
        Log.i(LOG_TAG, "TEST: loadJSON() called...");

        try {

            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API Key firstly from themoviedb.org.", Toast.LENGTH_SHORT).show();
                //pD.dismiss();
                return;
            }

            Client c = new Client();
            Service apiService = c.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    Log.i(LOG_TAG, "TEST: onResponse() called...");

                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);

                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }
                    //pD.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.i(LOG_TAG, "TEST: onFailure() called...");
                    Log.d("Error", t.getMessage());

                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(LOG_TAG, "TEST: onCreateOptionsMenu() called...");

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(LOG_TAG, "TEST: onOptionsItemSelected() called...");

        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.i(LOG_TAG, "TEST: onSharedPreferenceChanged() called...");

        Log.d(LOG_TAG, "TEST: Preferences updated!");
        checkSortOrder();
    }

    private void checkSortOrder() {
        Log.i(LOG_TAG, "TEST: checkSortOrder() called...");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );

        if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
            Log.d(LOG_TAG, "TEST: Sorting by most popular!");

            loadJSON();

        }else if(sortOrder.equals(this.getString(R.string.favorite))){
            Log.d(LOG_TAG, "TEST: Sorting by favorite!");

            initViews2();
        }
        else {
            Log.d(LOG_TAG, "TEST: Sorting by vote average!");

            loadJSON1();
        }
    }

    private void initViews2() {
        Log.i(LOG_TAG, "TEST: initViews2() called...");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        movieList= new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        favoriteDbHelper = new FavoriteDbHelper(activity);

        getAllFavorite();
    }

    public void getAllFavorite(){
        Log.i(LOG_TAG, "TEST: getAllFavorite() called...");

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params){
                Log.i(LOG_TAG, "TEST: doInBackground() called...");

                movieList.clear();
                movieList.addAll(favoriteDbHelper.getAllFavorite());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.i(LOG_TAG, "TEST: onPostExecute() called...");

                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "TEST: onResume() called...");

        super.onResume();

        if (movieList.isEmpty()) {
            checkSortOrder();
        } else {

        }
    }
}
