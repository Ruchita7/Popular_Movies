package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

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
    ArrayList<MovieParcelableObject> mMoviesList;


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

        mMoviesList = new ArrayList<>();
        mMovieAdapter = new ImageAdapter(getActivity(), R.layout.content_main, R.id.movie_content_imageview, mMoviesList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //  ButterKnife.bind(rootView);
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


                //    try {
                MovieParcelableObject movieItem = mMovieAdapter.getItem(position);
                ArrayList<String> movieDetailArray = new ArrayList<String>();
                HashMap<String, String> movieDetailMap = new HashMap<>();
                movieDetailMap.put(getString(R.string.title), movieItem.title);
                movieDetailMap.put(getString(R.string.background_path), movieItem.backgroundUrl);
                movieDetailMap.put(getString(R.string.overview), movieItem.synopsis);
                movieDetailMap.put(getString(R.string.votes), movieItem.userRating);
                movieDetailMap.put(getString(R.string.release_date), movieItem.releaseDate);
                movieDetailMap.put(getString(R.string.poster_path), movieItem.posterUrl);

                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra(getString(R.string.intent_movie_details), movieDetailMap);
                startActivity(intent);
               /* } catch (JSONException e) {
                    e.printStackTrace();
                }*/

            }
        });

        //Overriden to populate movie list with more than 20 records
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            //   private int visibleMovies = 20;      //Minimum no of movies to be fetched
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
                if ((mCurrentPage > 1 && mCurrentPage < 5) && ((totalItemCount - visibleItemCount) <= (firstVisibleItem + ConstantUtil.VISIBLE_MOVIES))) {
                    new PopularMoviesTask(getContext(), mMovieAdapter).execute(mCurrentPage++, mChosenOrder);

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

        PopularMoviesTask popularMoviesTask = new PopularMoviesTask(getContext(), mMovieAdapter);
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
        popularMoviesTask.execute(mCurrentPage, mChosenOrder);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
