package com.freelance.android.MovieApp.data;

import android.provider.BaseColumns;

/**
 * Created by Kyaw Khine on 09/30/2017.
 */

public class FavoriteContract {

    public static final class FavoriteEntry implements BaseColumns{

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIEID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USERRATING = "userRating";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_PLOT_SYNOPSIS = "overview";

    }

}
