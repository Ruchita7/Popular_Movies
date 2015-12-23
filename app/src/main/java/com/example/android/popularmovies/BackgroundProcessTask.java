package com.example.android.popularmovies;

import android.os.AsyncTask;

import com.example.android.popularmovies.util.MoviesUtility;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * Class for processing images in background thread using Async Task
 */
public class BackgroundProcessTask extends AsyncTask<Void, Void, Void> {

    private DetailMovieFragment mDetailMovieFragment;
    String mUrl;
    String mType;
    byte[] mBytes;

    /**
     * @param detailMovieFragment
     * @param url
     * @param type
     */
    BackgroundProcessTask(DetailMovieFragment detailMovieFragment, String url, String type) {

        mDetailMovieFragment = detailMovieFragment;
        mType = type;
        mUrl = url;
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(Void... params) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(mUrl).build();
        try {
            com.squareup.okhttp.Response response = client.newCall(request).execute();
            mBytes = MoviesUtility.getMovieImageInBytes(response.body().byteStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param aVoid
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mDetailMovieFragment.setImageBytes(mType, mBytes);
    }


}
