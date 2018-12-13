package com.freelance.android.MovieApp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kyaw Khine on 09/29/2017.
 */

public class TrailerResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<Trailer> trailerResults;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getTrailerResults() {
        return trailerResults;
    }

    public void setTrailerResults(List<Trailer> trailerResults) {
        this.trailerResults = trailerResults;
    }
}
