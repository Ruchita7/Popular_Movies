package com.example.android.popularmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    @Bind(R.id.movie_title_textView)
    TextView movie_title_textView;

    @Bind(R.id.movie_backdrop_imageView)
    ImageView movie_backdrop_imageView;

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

    public MovieDetailActivityFragment() {
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String date = null;
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_movie_details))) {
            HashMap<String, String> movieMap = (HashMap<String, String>) intent.getSerializableExtra("movie_details");
            movie_title_textView.setText(movieMap.get(getString(R.string.title)));
            String path = movieMap.get(getString((R.string.background_path))).replaceAll("/", "");

            Uri uri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                    appendPath(ConstantUtil.W500_SIZE).
                    appendPath(path).build();

            Picasso.with(getActivity()).load(uri).placeholder(resource_notfound).error(resource_notfound).into(movie_backdrop_imageView);

            plot_synopsis_textView.setText(movieMap.get(getString(R.string.overview)));
            user_rating_textView.setText(movieMap.get(getString(R.string.votes)));
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");

            String movieReleaseDate = null;
            try {
                date = movieMap.get(getString(R.string.release_date));
                Date parsedDate = format1.parse(date);
                DateFormat mediumDf = DateFormat.getDateInstance(DateFormat.MEDIUM);
                movieReleaseDate = mediumDf.format(parsedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            release_date_textView.setText(movieReleaseDate);
            path = movieMap.get(getString(R.string.poster_path)).replaceAll("/", "");

            uri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                    appendPath(ConstantUtil.W185_SIZE).
                    appendPath(path).build();

            Picasso.with(getActivity()).load(uri).placeholder(resource_notfound).error(resource_notfound).into(movie_poster_imageView);

        }
        return rootView;

    }

}
