package com.example.android.popularmovies;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.data.MoviesProvider;
import com.example.android.popularmovies.util.ConstantUtil;
import com.example.android.popularmovies.util.MoviesUtility;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    private boolean mTwoPane;

    private Integer mChosenOrder = 0;
    ArrayList<MovieParcelableObject> mMoviesList;
    MovieParcelableObject[] movieArray;
    private RecyclerView mRecyclerView;
    MoviesAdapter mMovieAdapter;
    FavoriteMoviesCursorAdapter mFavoriteMoviesCursorAdapter;
    private static final int CURSOR_LOADER_ID = 0;
    RecyclerView.LayoutManager mLayoutManager;
    private int mPosition = RecyclerView.NO_POSITION;
    private Parcelable mListState;

    public MovieFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
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

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.gridview_movies);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        mLayoutManager = new GridLayoutManager(getContext(), ConstantUtil.SPAN_COUNT);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (mMoviesList == null || mMoviesList.isEmpty()) {
            mMoviesList = new ArrayList<>();
        }


        checkNetworkStateForLoad();

        return rootView;

    }


    /**
     * Fetch movies on page load
     */
    @Override
    public void onStart() {
        checkNetworkStateForLoad();
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    /**
     * Determine internet connectivity
     */
    private void checkNetworkStateForLoad() {
        if (MoviesUtility.isNetworkAvailable(getActivity())) {
            populateMoviesList();
        } else {
            populateFavoriteMoviesList();
        }
    }
    /**
     * @param menu
     * @param inflater
     */

    /**
     * Populate movie details by calling Asynctask
     */
    private void populateMoviesList() {
        try {
            mChosenOrder = MoviesUtility.getPreferredSortOrder(getActivity());
            if (mChosenOrder == ConstantUtil.FAVORITE_MOVIE_CHOICE) {

                populateFavoriteMoviesList();
            } else {
                movieArray = new PopularMoviesTask(getActivity(), mMoviesList).execute(mChosenOrder).get();
                mMovieAdapter = new MoviesAdapter(getActivity(), movieArray, mChosenOrder);
                mMovieAdapter.notifyDataSetChanged();

                mRecyclerView.setAdapter(mMovieAdapter);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populate Favorite movies list
     */
    private void populateFavoriteMoviesList() {
        if (mFavoriteMoviesCursorAdapter == null) {
            mFavoriteMoviesCursorAdapter = new FavoriteMoviesCursorAdapter(getActivity(), null);
        }
        mRecyclerView.setAdapter(mFavoriteMoviesCursorAdapter);
    }


    /**
     * Method called on change in sorting order
     */
    public void sortOrderChanged() {

        checkNetworkStateForLoad();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }


    /**
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(ConstantUtil.MOVIE_LIST_KEY, mListState);
        super.onSaveInstanceState(outState);
    }

    /**
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MoviesProvider.Movies.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    /**
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mPosition != RecyclerView.NO_POSITION) {
            mRecyclerView.smoothScrollToPosition(mPosition);
        }
        if (mFavoriteMoviesCursorAdapter != null) {
            mFavoriteMoviesCursorAdapter.swapCursor(data);
        }

    }

    /**
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mFavoriteMoviesCursorAdapter != null) {
            mFavoriteMoviesCursorAdapter.swapCursor(null);
        }
    }

    /**
     * @param isTwoPane
     */
    public void setIsTwoPane(boolean isTwoPane) {
        mTwoPane = isTwoPane;
    }
}

