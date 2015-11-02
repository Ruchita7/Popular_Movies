package com.example.android.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Customized adapter class for fetching image views into grid view
 */
public class ImageAdapter extends ArrayAdapter<JSONObject> {

    Activity context;
    int resource;
    int imageViewResourceId;
    List<JSONObject> listObj;

    /**
     * @param context
     * @param resource
     * @param imageViewResourceId
     * @param listObj
     */
    public ImageAdapter(Activity context, int resource, int imageViewResourceId,
                        List<JSONObject> listObj) {
        super(context, resource, imageViewResourceId, listObj);
        this.context = context;
        this.resource = resource;
        this.imageViewResourceId = imageViewResourceId;
        this.listObj = listObj;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        ViewHolder viewHolder;
        String moviePoster = null;
        View view = null;
        try {
            JSONObject json = listObj.get(position);
            moviePoster = json.getString(ConstantUtil.MOVIE_POSTER).replaceAll("/", "");

            Uri uri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                    appendPath(ConstantUtil.W342_SIZE).
                    appendPath(moviePoster).build();

            if (convertView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                convertView = inflater.inflate(R.layout.content_main, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.movie_content_imageview);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Picasso.with(context).load(uri).into(viewHolder.imageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }


    /**
     * Static Imageholder class
     */
    static class ViewHolder {
        ImageView imageView;
    }
}
