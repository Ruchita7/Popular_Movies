package com.example.android.popularmovies;

/**
 * Created by dell on 11/5/2015.
 */

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.model.DiscoverMovies;
import com.example.android.popularmovies.util.ConstantUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Class to override AsyncTask for background thread implementation
 */
public class PopularMoviesTask extends AsyncTask<Integer, Void, MovieParcelableObject[]> {
    private final String LOG_TAG = PopularMoviesTask.class.getSimpleName();

    private Context context;
    private List<MovieParcelableObject> mMovieParcelableObjects;

    public PopularMoviesTask(Context context, List<MovieParcelableObject> movieParcelableObjects) {
        this.context = context;
        mMovieParcelableObjects = movieParcelableObjects;
        //this.recyclerView = recyclerView;
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected MovieParcelableObject[] doInBackground(Integer... params) {
        MovieParcelableObject[] movieParcelableObjects = null;
        ArrayList<MovieParcelableObject> movieParcelableObjectArrayList = null;
        String[] sortOrder = context.getResources().getStringArray(R.array.pref_sorting_values);
        int sort = Integer.parseInt(sortOrder[0]);

        movieParcelableObjectArrayList = getJsonFromUri(params[0]);
        if (movieParcelableObjectArrayList != null) {
            movieParcelableObjects = movieParcelableObjectArrayList.toArray(new MovieParcelableObject[movieParcelableObjectArrayList.size()]);
            return movieParcelableObjects;
        }
        return null;
    }


    /**
     * @param results
     */
    @Override
    protected void onPostExecute(MovieParcelableObject[] results) {
        if (results != null) {
            mMovieParcelableObjects = Arrays.asList(results);
        }
    }

    /**
     * @param sortOrder
     * @return
     */
    private ArrayList<MovieParcelableObject> getJsonFromUri(int sortOrder) {

        URL url = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        StringBuffer buffer = null;
        Uri buildUri = null;
        BufferedReader reader = null;
        String moviesJsonStr = "";

        ArrayList<MovieParcelableObject> movieList = null;
        try {
            String[] sortOrderArray = context.getResources().getStringArray(R.array.pref_sorting_values);
            for (int i = 1; i <= 5; i++) {
                if (sortOrder == Integer.parseInt(sortOrderArray[0])) {
                    buildUri = Uri.parse(ConstantUtil.BASE_URL_FOR_LISTING).buildUpon().
                            appendQueryParameter(ConstantUtil.SORT_ORDER, context.getResources().getString(R.string.most_popular)).
                            appendQueryParameter(ConstantUtil.API_KEY_PARAM, BuildConfig.POPULAR_MOVIE_API_KEY).
                            appendQueryParameter(ConstantUtil.PAGE_NUMBER, String.valueOf(i)).build();
                } else if (sortOrder == Integer.parseInt(sortOrderArray[1])) {
                    buildUri = Uri.parse(ConstantUtil.BASE_URL_FOR_LISTING).buildUpon().
                            appendQueryParameter(ConstantUtil.SORT_ORDER, context.getResources().getString(R.string.highest_rated)).
                            appendQueryParameter(ConstantUtil.VOTE_COUNT_PARAM, ConstantUtil.MIN_VOTES).
                            appendQueryParameter(ConstantUtil.API_KEY_PARAM, BuildConfig.POPULAR_MOVIE_API_KEY).
                            appendQueryParameter(ConstantUtil.PAGE_NUMBER, String.valueOf(i)).build();
                } else {
                    buildUri = Uri.parse(ConstantUtil.BASE_URL_FOR_LISTING).buildUpon().
                            appendQueryParameter(ConstantUtil.API_KEY_PARAM, BuildConfig.POPULAR_MOVIE_API_KEY).
                            appendQueryParameter(ConstantUtil.PAGE_NUMBER, String.valueOf(i)).build();
                }
                url = new URL(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(ConstantUtil.GET_REQUEST);
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0 || buffer == null) {
                    return null;
                }
                moviesJsonStr = buffer.toString();
                //     moviesJsonStr = moviesJsonStr.concat(pageResultJsonStr);
                Gson gson = new Gson();
                DiscoverMovies discoverMovies = gson.fromJson(moviesJsonStr, DiscoverMovies.class);
                if (movieList == null) {
                    movieList = new ArrayList<>();
                }
                movieList.addAll(discoverMovies.getResults());
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedURLException:", e);
            return null;
        } catch (ProtocolException e) {
            Log.e(LOG_TAG, "ProtocolException:", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException:", e);
            return null;
        } catch (Exception e) {

            Log.e(LOG_TAG, "Exception:", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream:", e);
                }
            }
        }
        return movieList;
    }
}