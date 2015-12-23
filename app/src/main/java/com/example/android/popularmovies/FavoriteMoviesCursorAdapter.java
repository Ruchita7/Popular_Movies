package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.api.CursorRecyclerViewAdapter;
import com.example.android.popularmovies.data.FavouriteMovie;
import com.example.android.popularmovies.util.ConstantUtil;

import java.io.ByteArrayInputStream;

/**
 * Cursor Adapter for populating Favorite Movies
 */
public class FavoriteMoviesCursorAdapter extends CursorRecyclerViewAdapter<FavoriteMoviesCursorAdapter.ViewHolder> {

    Context mContext;
    ViewHolder mVh;
    MovieParcelableObject mMovieParcelableObject;

    /**
     * @param context
     * @param cursor
     */
    public FavoriteMoviesCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    /**
     * ViewHolder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public final View mView;

        /**
         * @param view
         */
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_main, parent, false);

        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mPosition = vh.getPosition();
                onItemClick(v, vh.getPosition());
            }
        });
        return vh;
    }

    /**
     * @param viewHolder
     * @param cursor
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        DatabaseUtils.dumpCursor(cursor);
        byte[] posterImageByteArray = cursor.getBlob(cursor.getColumnIndex(FavouriteMovie.POSTER_IMAGE));
        ByteArrayInputStream imageStream = new ByteArrayInputStream(posterImageByteArray);
        Bitmap posterImage = BitmapFactory.decodeStream(imageStream);

        viewHolder.mImageView.setImageBitmap(posterImage);
    }

    /**
     * @param v
     * @param position
     */

    public void onItemClick(View v, int position) {

        final Cursor cursor = getItem(position);
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(ConstantUtil.MOVIE_LIST_KEY, position);
        ((CallBack) mContext).onItemSelected(ConstantUtil.FAVORITE_MOVIE_CHOICE, cursor.getString(cursor.getColumnIndex(FavouriteMovie.MOVIE_ID)));
    }

}
