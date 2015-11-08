package com.example.android.popularmovies;

/**
 * Created by dell on 11/5/2015.
 */

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.List;

/**
 * Class to override AsyncTask for background thread implementation
 */
public class PopularMoviesTask extends AsyncTask<Integer, Void, MovieParcelableObject[]> {
    private final String LOG_TAG = PopularMoviesTask.class.getSimpleName();

    private Context context;
    private ImageAdapter imageAdapter;

    public PopularMoviesTask(Context context, ImageAdapter imageAdapter)
    {
        this.context = context;
        this.imageAdapter = imageAdapter;
    }
    /**
     * @param params
     * @return
     */
    @Override
    protected MovieParcelableObject[] doInBackground(Integer... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movies[] = null;
        // Will contain the raw JSON response as a string.
        String moviesJsonStr = null;
        Uri buildUri = null;

        try {
          //  Context context = getApplicationContext();

            String[] sortOrder = context.getResources().getStringArray(R.array.pref_sorting_values);
            int sort = Integer.parseInt(sortOrder[0]);

            if (params[1] == Integer.parseInt(sortOrder[0])) {
               buildUri = Uri.parse(ConstantUtil.BASE_URL_FOR_LISTING).buildUpon().
                        appendQueryParameter(ConstantUtil.SORT_ORDER, context.getResources().getString(R.string.most_popular)).
                        appendQueryParameter(ConstantUtil.API_KEY_PARAM, BuildConfig.POPULAR_MOVIE_API_KEY).
                        appendQueryParameter(ConstantUtil.PAGE_NUMBER, params[0].toString()).build();
                  //buildUri = Uri.parse(ConstantUtil.BASE_URL_FOR_LISTING);
            } else if (params[1] == Integer.parseInt(sortOrder[1])) {

                buildUri = Uri.parse(ConstantUtil.BASE_URL_FOR_LISTING).buildUpon().
                        appendQueryParameter(ConstantUtil.SORT_ORDER, context.getResources().getString(R.string.highest_rated)).
                        appendQueryParameter(ConstantUtil.API_KEY_PARAM, BuildConfig.POPULAR_MOVIE_API_KEY).
                        appendQueryParameter(ConstantUtil.PAGE_NUMBER, params[0].toString()).build();
            } else {
                buildUri = Uri.parse(ConstantUtil.BASE_URL_FOR_LISTING).buildUpon().
                        appendQueryParameter(ConstantUtil.API_KEY_PARAM, BuildConfig.POPULAR_MOVIE_API_KEY).
                        appendQueryParameter(ConstantUtil.PAGE_NUMBER, params[0].toString()).build();

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
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        finally {
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
    private MovieParcelableObject[] getMovieDataFromJson(String movieJsonStr) throws JSONException {

        String moviePoster = null;
        JSONObject movieTempObj = null;
        String movieImagePath = null;
        Resources res;
        JSONObject movieJson = new JSONObject(movieJsonStr);
        res = context.getResources();
        MovieParcelableObject parcelableObject = null;
        JSONArray movieJsonArray = movieJson.getJSONArray(context.getResources().getString(R.string.result_arr));
     //   JSONObject[] moviePosterJSONArr = new JSONObject[movieJsonArray.length()];

        List<MovieParcelableObject> list = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            parcelableObject = new MovieParcelableObject(movieJsonArray.getJSONObject(i).getString(res.getString(R.string.poster_path)),
                    movieJsonArray.getJSONObject(i).getString(res.getString(R.string.title)),
                    movieJsonArray.getJSONObject(i).getString(res.getString(R.string.release_date)),
                    movieJsonArray.getJSONObject(i).getString(res.getString(R.string.votes)),
                    movieJsonArray.getJSONObject(i).getString(res.getString(R.string.overview)),
                    movieJsonArray.getJSONObject(i).getString(res.getString(R.string.background_path)));
            list.add(parcelableObject);
         /*   movieParcelableObject[i].posterUrl = movieJsonArray.getJSONObject(i).getString(context.getResources().getString(R.string.poster_path));
            movieParcelableObject[i].title = movieJsonArray.getJSONObject(i).getString(context.getResources().getString(R.string.title));
            movieParcelableObject[i].releaseDate = movieJsonArray.getJSONObject(i).getString(context.getResources().getString(R.string.release_date));
            movieParcelableObject[i].userRating = movieJsonArray.getJSONObject(i).getString(context.getResources().getString(R.string.votes));
            movieParcelableObject[i].synopsis = movieJsonArray.getJSONObject(i).getString(context.getResources().getString(R.string.overview));
            movieParcelableObject[i].backgroundUrl = movieJsonArray.getJSONObject(i).getString(context.getResources().getString(R.string.background_path));*/
        }

        MovieParcelableObject[] movieParcelableObject = list.toArray(new MovieParcelableObject[list.size()]);
        return  movieParcelableObject;
    }


    /**
     * @param results
     */
    @Override
    protected void onPostExecute(MovieParcelableObject[] results) {

        if (results != null) {
            imageAdapter.addAll(results);
        }
    }
}
