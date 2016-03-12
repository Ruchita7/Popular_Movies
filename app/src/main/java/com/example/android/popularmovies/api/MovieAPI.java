package com.example.android.popularmovies.api;

import com.example.android.popularmovies.MovieParcelableObject;
import com.example.android.popularmovies.model.MovieReviews;
import com.example.android.popularmovies.model.MovieVideos;
import com.example.android.popularmovies.util.ConstantUtil;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created as Endpoint for Retrofit
 */
public interface MovieAPI {

    /**
     * For getting movie detail by Id
     * @param movieId
     * @param apiKey
     * @return
     */
    @GET("movie/{movieId}")
    Call<MovieParcelableObject> chosenList(@Path("movieId") int movieId, @Query(ConstantUtil.API_KEY_PARAM) String apiKey);

    /**
     * Method for getting reviews for movie
     * @param movieId
     * @param apiKey
     * @return
     */
    @GET("movie/{movieId}/reviews")
    Call<MovieReviews> getMovieReviews(@Path("movieId") int movieId, @Query(ConstantUtil.API_KEY_PARAM) String apiKey);

    /**
     * Method for getting movie videos
     * @param movieId
     * @param apiKey
     * @return
     */
    @GET("movie/{movieId}/videos")
    Call<MovieVideos> getMovieVideos(@Path("movieId") int movieId, @Query(ConstantUtil.API_KEY_PARAM) String apiKey);

}
