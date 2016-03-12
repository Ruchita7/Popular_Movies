package com.example.android.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.api.CustomItemClickListener;
import com.example.android.popularmovies.util.ConstantUtil;
import com.squareup.picasso.Picasso;

/**
 * Adapter class for handling popular/highest rated lists
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> implements CustomItemClickListener {

    private MovieParcelableObject[] mMovieParcelableObjects;
    private Context mContext;
    private MovieParcelableObject movieParcelableObject;
    int mChosenOrder;

    /**
     * @param context
     * @param movieParcelableObjects
     * @param chosenOrder
     */
    public MoviesAdapter(Context context, MovieParcelableObject[] movieParcelableObjects, int chosenOrder) {
        mMovieParcelableObjects = movieParcelableObjects;
        mContext = context;
        mChosenOrder = chosenOrder;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public final View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.movie_content_imageview);

        }

    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_main, parent, false);

        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onItemClick(v, vh.getPosition());
            }
        });
        return vh;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String moviePoster = null;
        movieParcelableObject = mMovieParcelableObjects[position];
        if (movieParcelableObject.poster_path != null) {
            moviePoster = movieParcelableObject.poster_path.replaceAll("/", "");
        }
        Uri uri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                appendPath(ConstantUtil.W342_SIZE).
                appendPath(moviePoster).build();
        Picasso.with(mContext).load(uri).placeholder(R.drawable.resource_notfound).error(R.drawable.resource_notfound).into(holder.mImageView);
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {

        return mMovieParcelableObjects.length;
    }

    /**
     * @param v
     * @param position
     */
    @Override
    public void onItemClick(View v, int position) {
        Context context = v.getContext();
        movieParcelableObject = mMovieParcelableObjects[position];
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(ConstantUtil.MOVIE_LIST_KEY, position);
        ((CallBack) mContext).onItemSelected(mChosenOrder, String.valueOf(movieParcelableObject.id));
    }

    /**
     * @param v
     * @param resourceId
     * @param position
     */
    @Override
    public void onButtonClick(View v, int resourceId, int position) {

    }
}
