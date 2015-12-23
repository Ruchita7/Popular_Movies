package com.example.android.popularmovies.util;

/**
 * Constants class
 */
public class ConstantUtil {

    //Param for API KEY
    public static final String API_KEY_PARAM = "api_key";

    //Movie Listing URL
    public static final String URL_FOR_MOVIE_DETAIL = "http://api.themoviedb.org/3/";
    public static final String BASE_URL_FOR_LISTING = "http://api.themoviedb.org/3/discover/movie";

    //Movie Poster URL
    public static final String POSTER_URL = "http://image.tmdb.org/t/p/";

    //Image sizes
    public static final String W342_SIZE = "w342";
    public static final String W185_SIZE = "w185";
    public static final String W500_SIZE = "w500";

    //Request Parameters

    public static final String SORT_ORDER = "sort_by";
    public static final String PAGE_NUMBER = "page";
    public static final String VOTE_COUNT_PARAM = "vote_count.gte";
    public static final String MIN_VOTES = "100";
    public static final String GET_REQUEST = "GET";

    public static final String MOVIE_LIST_KEY = "movieListKey";
    public static final String MOVIE_DETAIL_KEY = "movieDetailKey";

    //Url Paths
    public static final String TRAILER_IMAGE_THUMBNAIL = "http://img.youtube.com/vi/";
    public static final String TRAILER_URL = "https://www.youtube.com/watch";
    public static final String SHARE_TRAILER = "https://youtu.be/";

    public static final int TRAILER_ADDITIONAL_HEIGHT = 50;
    public static final int REVIEWS_ADDITIONAL_HEIGHT = 200;

    public static final String MOVIE_POSTER = "poster";
    public static final String BACKDROP = "background";

    public static final int FAVORITE_MOVIE_CHOICE=2;
    public static final int IMAGE_WIDTH=185;
    public static  final int IMAGE_HEIGHT=278;

    public static final String ONE_PANE_MODE="One Pane";
    public static  final String TWO_PANE_MODE="Two Pane";

    public static final int SPAN_COUNT=2;
}
