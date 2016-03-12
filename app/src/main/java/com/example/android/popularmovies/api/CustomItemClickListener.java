package com.example.android.popularmovies.api;

import android.view.View;

/**
 * Custom interface ofor handling click events
 */
public interface CustomItemClickListener {

    public void onItemClick(View v, int position);
    public void onButtonClick(View v, int resourceId, int position);
}
