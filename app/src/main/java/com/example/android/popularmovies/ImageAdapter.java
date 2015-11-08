package com.example.android.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Customized adapter class for fetching image views into grid view
 */
public class ImageAdapter extends ArrayAdapter<MovieParcelableObject> {

    Activity context;
    int resource;
    int imageViewResourceId;
    List<MovieParcelableObject> listObj;

    /**
     * @param context
     * @param resource
     * @param imageViewResourceId
     * @param listObj
     */
    public ImageAdapter(Activity context, int resource, int imageViewResourceId,
                        List<MovieParcelableObject> listObj) {
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
        //   try {
        MovieParcelableObject json = listObj.get(position);
        moviePoster = json.posterUrl.replaceAll("/", "");

        Uri uri = Uri.parse(ConstantUtil.POSTER_URL).buildUpon().
                appendPath(ConstantUtil.W342_SIZE).
                appendPath(moviePoster).build();

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            if (getItemViewType(position) == ConstantUtil.IMAGE_VIEW_TYPE) {
                convertView = inflater.inflate(R.layout.content_main, parent, false);

                viewHolder = new ViewHolder(convertView);
                //viewHolder.imageView = (ImageView) convertView.findViewById(R.id.movie_content_imageview);

                convertView.setTag(viewHolder);
                Picasso.with(context).load(uri).placeholder(R.drawable.resource_notfound).error(R.drawable.resource_notfound).into(viewHolder.imageView);
            }
        } else {
            if (getItemViewType(position) == ConstantUtil.IMAGE_VIEW_TYPE) {
                viewHolder = (ViewHolder) convertView.getTag();
                Picasso.with(context).load(uri).placeholder(R.drawable.resource_notfound).error(R.drawable.resource_notfound).into(viewHolder.imageView);
            }
        }

        //    }
        //   Picasso.with(context).load(uri).into(viewHolder.imageView);


       /*  catch (JSONException e) {
            e.printStackTrace();
        }*/
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        int viewType = ConstantUtil.DEFAULT_VIEW_TYPE;
        if (listObj.get(position) instanceof MovieParcelableObject) {
            viewType = ConstantUtil.IMAGE_VIEW_TYPE;
        }
        return viewType;
    }

    @Override
    public int getViewTypeCount() {
        // return super.getViewTypeCount();
        return 1;
    }

    /**
     * Static Imageholder class
     */
    static class ViewHolder {
        ImageView imageView;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.movie_content_imageview);
        }
    }
}
