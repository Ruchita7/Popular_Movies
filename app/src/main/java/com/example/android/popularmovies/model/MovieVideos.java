package com.example.android.popularmovies.model;

import com.example.android.popularmovies.ReviewsParcelableObject;
import com.example.android.popularmovies.VideosParcelableObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dgnc on 12/1/2015.
 */
public class MovieVideos implements Serializable {
    private int id;

    private List<VideosParcelableObject> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<VideosParcelableObject> getResults() {
        return results;
    }

    public void setResults(List<VideosParcelableObject> results) {
        this.results = results;
    }
}
