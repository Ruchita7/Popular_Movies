package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    ImageAdapter mMovieAdapter = null;
    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    private Integer mCurrentPage = 1;
    int mChosenOrder;

    public MovieFragment() {
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mMovieAdapter = new ImageAdapter(getActivity(), R.layout.content_main, R.id.movie_content_imageview, new ArrayList<JSONObject>());
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMovieAdapter);

        //Overriden to fetch movie details
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    JSONObject jsonMovieItem = mMovieAdapter.getItem(position);
                    ArrayList<String> movieDetailArray = new ArrayList<String>();
                    HashMap<String, String> movieDetailMap = new HashMap<>();
                    movieDetailMap.put(getString(R.string.title), jsonMovieItem.getString(getString(R.string.title)));
                    movieDetailMap.put(getString(R.string.background_path), jsonMovieItem.getString(getString(R.string.background_path)));
                    movieDetailMap.put(getString(R.string.overview), jsonMovieItem.getString(getString(R.string.overview)));
                    movieDetailMap.put(getString(R.string.votes), jsonMovieItem.getString(getString(R.string.votes)));
                    movieDetailMap.put(getString(R.string.release_date), jsonMovieItem.getString(getString(R.string.release_date)));
                    movieDetailMap.put(getString(R.string.poster_path), jsonMovieItem.getString(getString(R.string.poster_path)));

                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra(getString(R.string.intent_movie_details), movieDetailMap);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        //Overriden to populate movie list with more than 20 records
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int visibleMovies = 20;      //Minimum no of movies to be fetched
            private int moviesFetched = 0;      //Count of movies fetched already
            private boolean isLoading = true;

            /**
             *
             * @param view
             * @param scrollState
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            /**
             *
             * @param view
             * @param firstVisibleItem
             * @param visibleItemCount
             * @param totalItemCount
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                totalItemCount = mMovieAdapter.getCount();
                if (mCurrentPage < 5) {
                    if (totalItemCount > moviesFetched) {

                        moviesFetched = totalItemCount;
                        mCurrentPage++;
                    }
                }
                if ((mCurrentPage > 1 && mCurrentPage < 5) && ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleMovies))) {
                    new PopularMoviesTask().execute(mCurrentPage++);

                }

            }
        });


        return rootView;

    }

    /**
     * Fetch movies on page load
     */
    @Override
    public void onStart() {
        super.onStart();
        populateMovies();
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    /**
     * Populate movie details by calling Asynctask
     */
    private void populateMovies() {

        PopularMoviesTask popularMoviesTask = new PopularMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (prefs != null) {
            String order = prefs.getString(getString(R.string.sorting_order), getString(R.string.pref_defaultValue));
            int order_value = Integer.parseInt(order);
            if (order_value >= 0) {
                Resources resources = getResources();
                mChosenOrder = Integer.parseInt(resources.getStringArray(R.array.pref_sorting_values)[order_value]);
            } else {
                mChosenOrder = order_value;
            }
        } else {
            mChosenOrder = Integer.parseInt(getString(R.string.pref_defaultValue));
        }
        popularMoviesTask.execute(mCurrentPage);
    }


    /**
     * Class to override AsyncTask for background thread implementation
     */
    public class PopularMoviesTask extends AsyncTask<Integer, Void, JSONObject[]> {
        private final String LOG_TAG = PopularMoviesTask.class.getSimpleName();

        /**
         * @param params
         * @return
         */
        @Override
        protected JSONObject[] doInBackground(Integer... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movies[] = null;
            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            Uri buildUri = null;

            try {
                Resources res = getResources();
                String[] sortOrder = res.getStringArray(R.array.pref_sorting_values);


                if (mChosenOrder == Integer.parseInt(sortOrder[0])) {
                    buildUri = Uri.parse(ConstantUtil.BASE_URL_FOR_LISTING).buildUpon().
                            appendQueryParameter(ConstantUtil.SORT_ORDER, getString(R.string.most_popular)).
                            appendQueryParameter(ConstantUtil.API_KEY_PARAM, BuildConfig.POPULAR_MOVIE_API_KEY).
                            appendQueryParameter(ConstantUtil.PAGE_NUMBER, mCurrentPage.toString()).build();
                } else if (mChosenOrder == Integer.parseInt(sortOrder[1])) {

                    buildUri = Uri.parse(ConstantUtil.BASE_URL_FOR_LISTING).buildUpon().
                            appendQueryParameter(ConstantUtil.SORT_ORDER, getString(R.string.highest_rated)).
                            appendQueryParameter(ConstantUtil.API_KEY_PARAM, BuildConfig.POPULAR_MOVIE_API_KEY).
                            appendQueryParameter(ConstantUtil.PAGE_NUMBER, mCurrentPage.toString()).build();
                } else {
                    buildUri = Uri.parse(ConstantUtil.BASE_URL_FOR_LISTING).buildUpon().
                            appendQueryParameter(ConstantUtil.API_KEY_PARAM, BuildConfig.POPULAR_MOVIE_API_KEY).
                            appendQueryParameter(ConstantUtil.PAGE_NUMBER, mCurrentPage.toString()).build();

                }

                URL url = new URL(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(ConstantUtil.GET_REQUEST);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
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

            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "MalformedURLException:", e);
                return null;
            } catch (ProtocolException e) {
                Log.e(LOG_TAG, "ProtocolException:", e);
                return null;
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException:", e);
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
                if (moviesJsonStr != null) {
                    try {

                        return getMovieDataFromJson(moviesJsonStr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }

        /**
         * Parse JSON String
         *
         * @param movieJsonStr
         * @return
         * @throws JSONException
         */
        private JSONObject[] getMovieDataFromJson(String movieJsonStr) throws JSONException {

            String moviePoster = null;
            JSONObject movieTempObj = null;
            String movieImagePath = null;

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieJsonArray = movieJson.getJSONArray(getString(R.string.result_arr));
            JSONObject[] moviePosterJSONArr = new JSONObject[movieJsonArray.length()];
            for (int i = 0; i < movieJsonArray.length(); i++) {
                moviePosterJSONArr[i] = movieJsonArray.getJSONObject(i);
            }

            return moviePosterJSONArr;
        }


        /**
         * @param results
         */
        @Override
        protected void onPostExecute(JSONObject[] results) {
            if (results != null) {
                mMovieAdapter.addAll(results);
            }
        }
    }


}
