package com.example.android.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.api.MovieAPI;
import com.example.android.popularmovies.data.FavouriteMovie;
import com.example.android.popularmovies.data.MoviesProvider;
import com.example.android.popularmovies.model.MovieReviews;
import com.example.android.popularmovies.model.MovieVideos;
import com.example.android.popularmovies.util.ConstantUtil;
import com.example.android.popularmovies.util.MoviesUtility;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = DetailMovieFragment.class.getSimpleName();
    public static final String FRAG_MOVIE_FETCH_DETAILS = "FRAG_MOVIE_FETCH_DETAILS";
    private static final int DETAIL_LOADER_ID = 0;
    @Bind(R.id.movie_title_textView)
    TextView movie_title_textView;
    @BindDrawable(R.drawable.resource_notfound)
    Drawable resource_notfound;
    @Bind(R.id.plot_synopsis_textView)
    TextView plot_synopsis_textView;
    @Bind(R.id.user_rating_textView)
    TextView user_rating_textView;
    @Bind(R.id.release_date_textView)
    TextView release_date_textView;
    @Bind(R.id.movie_poster_imageView)
    ImageView movie_poster_imageView;
    @Bind(R.id.movie_backdrop_imageView)
    ImageView movie_backdrop_imageView;
    @Bind(R.id.reviews_list)
    ListView mReviewsListView;
    @Bind(R.id.trailers_list)
    ListView mTrailersListView;
    @Bind(R.id.user_rating_image)
    ImageView user_rating_imageView;
    @Bind(R.id.release_date_label)
    TextView release_date_label;
    MovieTrailersAdapter movieTailersAdapter;
    MovieParcelableObject mMovieParcelableObject;
    @Bind(R.id.play_trailer_main_imageView)
    ImageView mPlayTrailerMainImageView;
    @Bind(R.id.vote_count_textView)
    TextView vote_count_textView;
    @Bind(R.id.max_count_textView)
    TextView max_count_textView;
    @Bind(R.id.reviews_title)
    TextView reviews_title;
    ArrayList<ReviewsParcelableObject> mReviewsList;
    List<VideosParcelableObject> mVideosList;
    String mVideoKey;
    byte[] mPosterImage = null;
    byte[] mBackgroundImage;
    boolean mPane;
    private ReviewsAdapter mReviewsAdapter;
    private Context mContext;
    private int mChosenOrder;
    private String mMovieId;

    public DetailMovieFragment() {
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        //  Log.v(LOG_TAG,"onCreate in DetailMovieFrag");
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem;
        inflater.inflate(R.menu.menu_movie_detail, menu);

        String selectionClause = FavouriteMovie.MOVIE_ID + "=?";
        String[] selectionArgs = {""};

        selectionArgs[0] = String.valueOf(mMovieId);
        Cursor cursor = mContext.getContentResolver().query(MoviesProvider.Movies.CONTENT_URI, null, selectionClause, selectionArgs, null);

        //check if movie is already added to favorites
        if (cursor != null && cursor.getCount() > 0) {
            menuItem = menu.findItem(R.id.action_favorite);
            menuItem.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_favorite_white));
            cursor.close();
        } else {
            menuItem = menu.findItem(R.id.action_favorite);
            menuItem.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_favorite_border_white));
        }
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_favorite:
                actionPerform();
                return true;

            case R.id.action_share:
                shareMovieDetails(mMovieParcelableObject, getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method to add/delete movie from favorites
     */
    private void actionPerform() {
        String selectionClause = FavouriteMovie.MOVIE_ID + "=?";
        String[] selectionArgs = {""};
        selectionArgs[0] = String.valueOf(mMovieParcelableObject.id);
        Cursor cursor = getActivity().getContentResolver().query(MoviesProvider.Movies.CONTENT_URI, null, selectionClause, selectionArgs, null);

        ActionMenuItemView menuItem = (ActionMenuItemView) getActivity().findViewById(R.id.action_favorite);
        if (null == cursor || cursor.getCount() == 0) {
            addToFavorites();
        } else {
            int rowsDeleted = getActivity().getContentResolver().delete(MoviesProvider.Movies.CONTENT_URI, selectionClause, selectionArgs);
            if (rowsDeleted > 0) {
                menuItem.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_favorite_border_white));
                Toast.makeText(getActivity(), mMovieParcelableObject.original_title + " removed from Favorites", Toast.LENGTH_SHORT).show();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * @param menu
     */
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
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


        mReviewsList = new ArrayList<>();
        List<String> chosenList = null;

        final View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);


        Bundle arguments = getArguments();
        if (arguments != null) {
            chosenList = arguments.getStringArrayList(DetailMovieFragment.FRAG_MOVIE_FETCH_DETAILS);
            if (chosenList != null && !chosenList.isEmpty()) {
                mChosenOrder = Integer.parseInt(chosenList.get(0));
                mMovieId = chosenList.get(1);

                if (chosenList.get(2).equalsIgnoreCase(ConstantUtil.TWO_PANE_MODE)) {
                    mPane = true;
                }
            }
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(ConstantUtil.MOVIE_DETAIL_KEY)) {
            mMovieParcelableObject = savedInstanceState.getParcelable(ConstantUtil.MOVIE_DETAIL_KEY);
        }
        return rootView;

    }

    /**
     * Method to play movie trailer
     */
    @OnClick(R.id.play_trailer_main_imageView)
    public void playTrailer() {
        Uri videoUri;
        String videoUrl;
        videoUri = Uri.parse(ConstantUtil.TRAILER_URL).buildUpon().appendQueryParameter("v", mVideoKey).build();
        videoUrl = videoUri.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra("VIDEO_ID", mVideoKey);
        if (getActivity().getPackageManager() != null) {
            startActivity(intent);
        }
    }

    /**
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(ConstantUtil.MOVIE_DETAIL_KEY, mMovieParcelableObject);
        super.onSaveInstanceState(outState);

    }

    /**
     * Method to add movie to favorites collection
     */
    public void addToFavorites() {


        Uri uri;
        String posterPath = "";
        String backdropPath = "";
        if (mMovieParcelableObject.poster_path != null) {
            posterPath = mMovieParcelableObject.poster_path.replaceAll("/", "");
        }
        Uri posterUri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                appendPath(ConstantUtil.W342_SIZE).
                appendPath(posterPath).build();
        BackgroundProcessTask backgroundProcessTask = new BackgroundProcessTask(this, posterUri.toString(), ConstantUtil.MOVIE_POSTER);
        backgroundProcessTask.execute();

        if (mMovieParcelableObject.backdrop_path != null) {
            backdropPath = mMovieParcelableObject.backdrop_path.replaceAll("/", "");
        }
        uri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                appendPath(ConstantUtil.W500_SIZE).appendPath(backdropPath).build();
        backgroundProcessTask = new BackgroundProcessTask(this, uri.toString(), ConstantUtil.BACKDROP);
        backgroundProcessTask.execute();
    }

    /**
     * Share movie details via intent
     *
     * @param movieParcelableObject
     * @param context
     */
    public void shareMovieDetails(MovieParcelableObject movieParcelableObject, Activity context) {
        String movieReleaseDate = null;
        String posterPath = null;
        String path = null;
        Uri posterUri = null;

        path = movieParcelableObject.poster_path;

        if (path != null) {
            posterPath = path.replaceAll("/", "");
            posterUri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                    appendPath(ConstantUtil.W342_SIZE).
                    appendPath(posterPath).build();
        }
        Uri videoUri = null;
        String videoUrl = "";
        if (mVideoKey != null) {
            videoUri = Uri.parse(ConstantUtil.SHARE_TRAILER).buildUpon().appendPath(mVideoKey).build();
            videoUrl = videoUri.toString();
        }

        movieReleaseDate = MoviesUtility.formatDate(movieParcelableObject.release_date);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("text/plain");
        //   intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Watch : " + movieParcelableObject.original_title);
        String shareText = "";

        shareText += "\nSynopsis : " + movieParcelableObject.overview;


        if (videoUrl != null) {
            shareText += "\n\n" + videoUrl;
        }
        shareText += "\n\nUser Rating : " + movieParcelableObject.vote_average +
                "\nRelease Date : " + movieReleaseDate;
        if (posterUri != null) {
            shareText += "\n" + posterUri.toString();
        }
        intent.putExtra(Intent.EXTRA_TEXT,
                shareText);
        if (context.getPackageManager() != null) {

            context.startActivity(intent);
        }

    }

    /**
     * Called when sorting order is change
     *
     * @param sortOrder
     */
    public void sortOrderChanged(int sortOrder) {
        mChosenOrder = sortOrder;
    }

    /**
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (mChosenOrder == ConstantUtil.FAVORITE_MOVIE_CHOICE) {
            if (mMovieId != null) {
                String selectionClause = FavouriteMovie.MOVIE_ID + "=?";
                String[] selectionArgs = {""};
                selectionArgs[0] = mMovieId;
                return new CursorLoader(getActivity(), MoviesProvider.Movies.CONTENT_URI,
                        null,
                        selectionClause,
                        selectionArgs,
                        null);
            }
        }
        return null;
    }

    /**
     * Fetch movie information by movie Id
     *
     * @param movieId
     */
    private void retrieveMovieInformation(String movieId) {
        changeVisibility();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUtil.URL_FOR_MOVIE_DETAIL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Call call = null;
        MovieAPI movieAPIService = retrofit.create(MovieAPI.class);
        call = movieAPIService.chosenList(Integer.parseInt(movieId), BuildConfig.POPULAR_MOVIE_API_KEY);
        call.enqueue(new Callback<MovieParcelableObject>() {
            @Override
            public void onResponse(Response<MovieParcelableObject> response, Retrofit retrofit) {
                MovieParcelableObject movieParcelableObject = response.body();

                if (movieParcelableObject == null) {
                    ResponseBody responseErrBody = response.errorBody();
                    if (responseErrBody != null) {

                        try {
                            String str = response.errorBody().string();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, e.getMessage());
                            return;
                        }
                    }
                }

                mMovieParcelableObject = movieParcelableObject;
                getMovieDetails(mMovieParcelableObject);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    /**
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (mChosenOrder == ConstantUtil.FAVORITE_MOVIE_CHOICE) {
            if (!data.moveToFirst()) {
                return;
            }
            if (data != null && data.moveToFirst()) {
                String posterPath = data.getString(data.getColumnIndex(FavouriteMovie.POSTER_PATH));
                byte[] posterImageByteArray = data.getBlob(data.getColumnIndex(FavouriteMovie.POSTER_IMAGE));
                byte[] backdropImageByteArray = data.getBlob(data.getColumnIndex(FavouriteMovie.BACKGROUND_IMAGE));
                String overview = data.getString((data.getColumnIndex(FavouriteMovie.MOVIE_DESC)));
                String movieId = data.getString(data.getColumnIndex(FavouriteMovie.MOVIE_ID));
                String movieReleaseDate = data.getString(data.getColumnIndex(FavouriteMovie.MOVIE_RELEASE_DATE));
                String title = data.getString(data.getColumnIndex(FavouriteMovie.TITLE));
                String voteAvg = data.getString(data.getColumnIndex(FavouriteMovie.VOTE_AVERAGE));
                String voteCount = data.getString(data.getColumnIndex(FavouriteMovie.VOTE_COUNT));

                mMovieParcelableObject = new MovieParcelableObject(posterPath, title,
                        movieReleaseDate, voteAvg,
                        overview, null, Integer.parseInt(movieId), Integer.parseInt(voteCount), backdropImageByteArray, posterImageByteArray);
                getMovieDetails(mMovieParcelableObject);
                changeVisibility();
            }
        }
    }

    /**
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void changeVisibility() {
        release_date_label.setVisibility(View.VISIBLE);
        user_rating_imageView.setVisibility(View.VISIBLE);
        movie_title_textView.setVisibility(View.VISIBLE);
        max_count_textView.setVisibility(View.VISIBLE);
    }

    /**
     * Get movie details
     *
     * @param movieParcelableObject
     */
    private void getMovieDetails(MovieParcelableObject movieParcelableObject) {
        String backgroundPath = "";
        String posterPath = "";
        String path;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUtil.URL_FOR_MOVIE_DETAIL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieAPI movieAPIService = retrofit.create(MovieAPI.class);

        Call call = movieAPIService.getMovieVideos(movieParcelableObject.id, BuildConfig.POPULAR_MOVIE_API_KEY);
        call.enqueue(new Callback<MovieVideos>() {
            @Override
            public void onResponse(Response<MovieVideos> response, Retrofit retrofit) {
                MovieVideos movieVideos = response.body();

                if (movieVideos == null) {
                    ResponseBody responseErrBody = response.errorBody();
                    if (responseErrBody != null) {

                        try {
                            String str = response.errorBody().string();
                        } catch (IOException e) {
                            //                Log.e(LOG_TAG, e.getMessage());
                            return;
                        }
                    }
                }

                mVideosList = (ArrayList) movieVideos.getResults();

                if (mVideosList == null || mVideosList.isEmpty()) {
                    mVideosList = new ArrayList<VideosParcelableObject>();
                } else {
                    for (VideosParcelableObject videosParcelableObject : mVideosList) {
                        if (videosParcelableObject.name.equalsIgnoreCase("Official Trailer")) {
                            mVideoKey = videosParcelableObject.key;
                            break;
                        }
                    }
                }
                movieTailersAdapter = new MovieTrailersAdapter(getActivity(), mMovieParcelableObject, mVideosList);
                mTrailersListView.setAdapter(movieTailersAdapter);
                MoviesUtility.getListViewSize(mTrailersListView, ConstantUtil.TRAILER_ADDITIONAL_HEIGHT);
                movieTailersAdapter.notifyDataSetChanged();


                if ((mVideosList != null && !mVideosList.isEmpty()) && mVideoKey == null) {
                    mVideoKey = mVideosList.get(0).key;
                }
                if (mVideoKey != null) {
                    mPlayTrailerMainImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });


        movie_title_textView.setText(movieParcelableObject.original_title);

        if (mChosenOrder != ConstantUtil.FAVORITE_MOVIE_CHOICE) {
            path = movieParcelableObject.poster_path;

            if (path != null) {
                posterPath = path.replaceAll("/", "");
            }
            Uri uri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                    appendPath(ConstantUtil.W185_SIZE).
                    appendPath(posterPath).build();
            Picasso.with(getActivity()).load(uri).placeholder(resource_notfound).error(resource_notfound).into(movie_poster_imageView);

            path = null;
            path = movieParcelableObject.backdrop_path;
            if (path != null) {
                backgroundPath = path.replaceAll("/", "");
            }

            uri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                    appendPath(ConstantUtil.W500_SIZE).
                    appendPath(backgroundPath).build();


            Picasso.with(getActivity()).load(uri).placeholder(resource_notfound).error(resource_notfound).into(movie_backdrop_imageView);


        } else {
            ByteArrayInputStream imageStream;
            byte[] backdropImageByteArray = movieParcelableObject.backgroundImage;
            if (backdropImageByteArray != null) {
                imageStream = new ByteArrayInputStream(backdropImageByteArray);
                Bitmap backdropImage = BitmapFactory.decodeStream(imageStream);
                movie_backdrop_imageView.setImageBitmap(backdropImage);
            }
            byte[] posterImageByteArray = movieParcelableObject.posterImage;
            if (posterImageByteArray != null) {
                imageStream = new ByteArrayInputStream(posterImageByteArray);
                Bitmap posterImage = BitmapFactory.decodeStream(imageStream);
                //movie_poster_imageView.setImageBitmap(posterImage);
                Bitmap image = MoviesUtility.getResizedBitmap(posterImage, ConstantUtil.IMAGE_WIDTH, ConstantUtil.IMAGE_HEIGHT);
                movie_poster_imageView.setImageBitmap(image);
            }
        }

        plot_synopsis_textView.setText(movieParcelableObject.overview);
        user_rating_textView.setText(movieParcelableObject.vote_average);

        vote_count_textView.setText(String.valueOf(movieParcelableObject.vote_count));
        String movieReleaseDate = MoviesUtility.formatDate(movieParcelableObject.release_date);
        release_date_textView.setText(movieReleaseDate);


        call = null;
        call = movieAPIService.getMovieReviews(movieParcelableObject.id, BuildConfig.POPULAR_MOVIE_API_KEY);
        call.enqueue(new Callback<MovieReviews>() {
            @Override
            public void onResponse(Response<MovieReviews> response, Retrofit retrofit) {
                MovieReviews movieReviews = response.body();

                if (movieReviews == null) {
                    ResponseBody responseErrBody = response.errorBody();
                    if (responseErrBody != null) {

                        try {
                            String str = response.errorBody().string();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, e.getMessage());
                            return;
                        }
                    }
                }

                mReviewsList = (ArrayList) movieReviews.getResults();
                if (mReviewsList != null && !mReviewsList.isEmpty()) {
                    mReviewsAdapter = new ReviewsAdapter(getActivity(), mReviewsList);
                    mReviewsListView.setAdapter(mReviewsAdapter);
                    MoviesUtility.getListViewSize(mReviewsListView, ConstantUtil.REVIEWS_ADDITIONAL_HEIGHT);
                    mReviewsAdapter.notifyDataSetChanged();
                    reviews_title.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    /**
     * Method for inserting movie details into DB
     *
     * @param movieParcelableObject
     * @param context
     */
    private void insertValues(MovieParcelableObject movieParcelableObject, Activity context) {
        Uri uri;
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavouriteMovie.MOVIE_ID, mMovieParcelableObject.id);
        contentValues.put(FavouriteMovie.BACKGROUND_IMAGE, mBackgroundImage);
        contentValues.put(FavouriteMovie.MOVIE_DESC, mMovieParcelableObject.overview);
        contentValues.put(FavouriteMovie.MOVIE_RELEASE_DATE, mMovieParcelableObject.release_date);
        contentValues.put(FavouriteMovie.POSTER_PATH, movieParcelableObject.poster_path);
        contentValues.put(FavouriteMovie.POSTER_IMAGE, mPosterImage);
        contentValues.put(FavouriteMovie.TITLE, mMovieParcelableObject.original_title);
        contentValues.put(FavouriteMovie.VOTE_AVERAGE, mMovieParcelableObject.vote_average);
        contentValues.put(FavouriteMovie.VOTE_COUNT, mMovieParcelableObject.vote_count);
        uri = context.getContentResolver().insert(MoviesProvider.Movies.CONTENT_URI, contentValues);
        ActionMenuItemView menuItem = (ActionMenuItemView) context.findViewById(R.id.action_favorite);
        menuItem.setIcon(context.getResources().getDrawable(R.drawable.ic_favorite_white));

        Toast.makeText(getActivity(), mMovieParcelableObject.original_title + " added to Favorites", Toast.LENGTH_SHORT).show();
        // return uri;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mChosenOrder != ConstantUtil.FAVORITE_MOVIE_CHOICE) {
            if (mMovieId != null) {
                retrieveMovieInformation(mMovieId);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Method to set byte arrays for image views
     *
     * @param type
     * @param bytes
     */
    public void setImageBytes(String type, byte[] bytes) {
        if (type.equalsIgnoreCase(ConstantUtil.BACKDROP)) {
            mBackgroundImage = bytes;
        } else {
            mPosterImage = bytes;
        }
        if (mPosterImage != null && mBackgroundImage != null) {
            insertValues(mMovieParcelableObject, getActivity());
        }
    }
}

