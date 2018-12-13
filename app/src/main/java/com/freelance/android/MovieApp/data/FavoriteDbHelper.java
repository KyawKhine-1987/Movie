package com.freelance.android.MovieApp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.freelance.android.MovieApp.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyaw Khine on 09/30/2017.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = FavoriteDbHelper.class.getName();

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(LOG_TAG, "TEST: FavoriteDbHelper() called...");
    }

    public void Open() {
        Log.i(LOG_TAG, "TEST: Database Opened() called...");

        db = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOG_TAG, "TEST: Database Closed() called...");

        dbhandler.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(LOG_TAG, "TEST: onCreate() called...");

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + "(" +
                FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID + " INTEGER, " +
                FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_USERRATING + " REAL NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL " + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(LOG_TAG, "TEST: onUpgrade() called...");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + FavoriteContract.FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addFavorite(Movie movie) {
        Log.i(LOG_TAG, "TEST: addFavorite() called...");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, movie.getId());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movie.getOriginalTitle());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_USERRATING, movie.getVoteAverage());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, movie.getOverview());

        db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteFavorite(int id) {
        Log.i(LOG_TAG, "TEST: deleteFavorite() called...");

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, FavoriteContract.FavoriteEntry.COLUMN_MOVIEID + "=" + id, null);
    }

    public List<Movie> getAllFavorite() {
        Log.i(LOG_TAG, "TEST: getAllFavorite() called...");

        String[] columns = {
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID,
                FavoriteContract.FavoriteEntry.COLUMN_TITLE,
                FavoriteContract.FavoriteEntry.COLUMN_USERRATING,
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH,
                FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS
        };

        String sortOrder = FavoriteContract.FavoriteEntry._ID + " ASC";

        List<Movie> favoriteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (c.moveToFirst()) {

            do {
                Movie m = new Movie();
                m.setId(Integer.parseInt(c.getString(c.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID))));
                m.setOriginalTitle(c.getString(c.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)));
                m.setVoteAverage(Double.parseDouble(c.getString(c.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_USERRATING))));
                m.setPosterPath(c.getString(c.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH)));
                m.setOverview(c.getString(c.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS)));

                favoriteList.add(m);
            } while (c.moveToNext());

        }
        c.close();
        db.close();
        return favoriteList;
    }
}
