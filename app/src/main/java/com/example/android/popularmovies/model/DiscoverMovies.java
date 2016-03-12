package com.example.android.popularmovies.model;

import com.example.android.popularmovies.MovieParcelableObject;

import java.io.Serializable;
import java.util.List;

/**
 * Model class to store Json movie results
 */
public class DiscoverMovies implements Serializable {

    private int page;

    private List<MovieParcelableObject> results;

    private int total_pages;

    private int total_results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieParcelableObject> getResults() {
        return results;
    }

    public void setResults(List<MovieParcelableObject> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
