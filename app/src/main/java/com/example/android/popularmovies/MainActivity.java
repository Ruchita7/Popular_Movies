package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.util.ConstantUtil;
import com.example.android.popularmovies.util.MoviesUtility;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CallBack {

    private static final String MOVIE_DETAIL_FRAGMENT_TAG = "DMFTAG";

    private boolean mTwoPane;
    private Integer mChosenOrder;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChosenOrder = MoviesUtility.getPreferredSortOrder(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

        //Condition to check for tablet
        if (findViewById(R.id.movies_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movies_detail_container, new DetailMovieFragment(), MOVIE_DETAIL_FRAGMENT_TAG)
                        .commit();
            }

        } else {
            mTwoPane = false;
        }

        MovieFragment movieFragment = (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
        movieFragment.setIsTwoPane(mTwoPane);
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Integer chosenOrder = MoviesUtility.getPreferredSortOrder(this);

        if (chosenOrder != null && mChosenOrder != chosenOrder) {
            if (!MoviesUtility.isNetworkAvailable(this)) {
                chosenOrder = ConstantUtil.FAVORITE_MOVIE_CHOICE;
            }
            MovieFragment movieFragment = (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
            if (null != movieFragment) {
                movieFragment.sortOrderChanged();
            }
            DetailMovieFragment detailMovieFragment = (DetailMovieFragment) getSupportFragmentManager().findFragmentByTag(MOVIE_DETAIL_FRAGMENT_TAG);
            if (null != detailMovieFragment) {
                detailMovieFragment.sortOrderChanged(chosenOrder);
            }
            mChosenOrder = chosenOrder;
        }
    }

    /**
     * Implementing call back interface method
     *
     * @param chosenOrder
     * @param movieId
     */
    @Override
    public void onItemSelected(int chosenOrder, String movieId) {
        ArrayList<String> chosenValueList = null;
        chosenValueList = new ArrayList<>();
        chosenValueList.add(String.valueOf(chosenOrder));
        chosenValueList.add(movieId);

        Bundle bundle = new Bundle();
        if (mTwoPane) {
            chosenValueList.add(ConstantUtil.TWO_PANE_MODE);
            bundle.putStringArrayList(DetailMovieFragment.FRAG_MOVIE_FETCH_DETAILS, chosenValueList);
            DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
            detailMovieFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.movies_detail_container, detailMovieFragment, MOVIE_DETAIL_FRAGMENT_TAG).
                    commit();
        } else {
            chosenValueList.add(ConstantUtil.ONE_PANE_MODE);
            Intent intent = new Intent(this, MovieDetailActivity.class).putStringArrayListExtra(MovieDetailActivity.ACTIVITY_MOVIE_FETCH_DETAILS, chosenValueList);
            startActivity(intent);
        }
    }
}
