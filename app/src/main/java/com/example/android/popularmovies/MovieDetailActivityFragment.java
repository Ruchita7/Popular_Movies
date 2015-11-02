package com.example.android.popularmovies;

import android.content.Intent;
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
import java.util.HashMap;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {


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
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_movie_details))) {
            HashMap<String, String> movieMap = (HashMap<String, String>) intent.getSerializableExtra("movie_details");

            TextView titleTextView = (TextView) rootView.findViewById(R.id.movie_title_textView);
            titleTextView.setText(movieMap.get(getString(R.string.title)));

            ImageView backdropImageView = (ImageView) rootView.findViewById(R.id.movie_backdrop_imageView);
            String path = movieMap.get(getString((R.string.background_path))).replaceAll("/", "");


            Uri uri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                    appendPath(ConstantUtil.W500_SIZE).
                    appendPath(path).build();

            Picasso.with(getActivity()).load(uri).into(backdropImageView);

            TextView synopsisTextView = (TextView) rootView.findViewById(R.id.plot_synopsis_textView);
            synopsisTextView.setText(movieMap.get(getString(R.string.overview)));

            TextView ratingTextView = (TextView) rootView.findViewById(R.id.user_rating_textView);
            ratingTextView.setText( movieMap.get(getString(R.string.votes)));

            TextView releaseDateTextView = (TextView) rootView.findViewById(R.id.release_date_textView);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-mm-dd");

            String movieReleaseDate = null;
            try {
                date = movieMap.get(getString(R.string.release_date));
                Date parsedDate = format1.parse(date);
                DateFormat mediumDf = DateFormat.getDateInstance(DateFormat.MEDIUM);
                movieReleaseDate =  mediumDf.format(parsedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            releaseDateTextView.setText(movieReleaseDate);

            ImageView posterImageView = (ImageView) rootView.findViewById(R.id.movie_poster_imageView);
            path = movieMap.get(getString(R.string.poster_path)).replaceAll("/", "");

            uri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                    appendPath(ConstantUtil.W185_SIZE).
                    appendPath(path).build();

            Picasso.with(getActivity()).load(uri).into(posterImageView);

        }
        return rootView;

    }

}
