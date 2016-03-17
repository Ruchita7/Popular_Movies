package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.api.CustomItemClickListener;
import com.example.android.popularmovies.util.ConstantUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * MovieTrailersAdapter for Popular Movies
 * @author Ruchita_Maheshwary
 *
 */
 
public class MovieTrailersAdapter extends ArrayAdapter<VideosParcelableObject> implements CustomItemClickListener {

    private Activity mContext;
    LayoutInflater mLayoutInflater;
    //int mResourceId;
    private List<VideosParcelableObject> mTrailersList;
    private VideosParcelableObject mVideosParcelableObject;
    private MovieParcelableObject mMovieParcelableObject;
    private String mVideoKey;

    public MovieTrailersAdapter(Activity context) {
        super(context, 0, new ArrayList<VideosParcelableObject>());
        mContext = context;
    }

    MovieTrailersAdapter(Activity context, MovieParcelableObject movieParcelableObject, List<VideosParcelableObject> trailersList) {
        super(context, 0, trailersList);
        mContext = context;
        mMovieParcelableObject = movieParcelableObject;
        //    mResourceId = resourceId;
        mTrailersList = trailersList;
    }


    public static class ViewHolder {
        public final ImageView trailerImageView;
        public final ImageView playButtonImageView;
        public final TextView videoTypeTextView;
        public final ImageView shareVideoImageView;

        // public final View mView;

        public ViewHolder(View view) {
            //    super(view);
            //   mView = view;
            trailerImageView = (ImageView) view.findViewById(R.id.trailer_id);
            playButtonImageView = (ImageView) view.findViewById(R.id.play_button);
            videoTypeTextView = (TextView) view.findViewById(R.id.video_type);
            shareVideoImageView = (ImageView) view.findViewById(R.id.share_video);
        }


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        ViewHolder viewHolder;
        String moviePoster = null;
        View view = null;
        Uri uri = null;

        //   try {
        mVideosParcelableObject = mTrailersList.get(position);
        // moviePoster = json.posterUrl.replaceAll("/", "");
        String videoKey = mTrailersList.get(position).key;
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.movie_trailers_detail, parent, false);

            viewHolder = new ViewHolder(convertView);
            //viewHolder.imageView = (ImageView) convertView.findViewById(R.id.movie_content_imageview);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        uri = Uri.parse(ConstantUtil.TRAILER_IMAGE_THUMBNAIL).buildUpon().appendPath(videoKey).appendPath("mqdefault.jpg").build();


        String imagePath = uri.toString();
        Picasso.with(mContext).load(imagePath).placeholder(R.drawable.resource_notfound).error(R.drawable.resource_notfound).into(viewHolder.trailerImageView);
      /*  videoUri = Uri.parse(ConstantUtil.TRAILER_URL).buildUpon().appendQueryParameter("v",videoKey).build();
        videoUrl = videoUri.toString();*/

        viewHolder.videoTypeTextView.setText("Trailer " + (position + 1));

            viewHolder.trailerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClick(v, R.id.trailer_id,position);
                }
            });

        viewHolder.shareVideoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v,R.id.share_video,position);
            }
        });
        return convertView;
    }

    @Override
    public void onItemClick(View v, int position) {

    }

    @Override
    public void onButtonClick(View v, int resourceId,int position) {
        VideosParcelableObject videosParcelableObject =  mTrailersList.get(position);
       String videoUrl;
        Uri videoUri = null;
        String videoKey = videosParcelableObject.key;
        videoUri = Uri.parse(ConstantUtil.TRAILER_URL).buildUpon().appendQueryParameter("v", videoKey).build();
       //
       // Log.i("LOG_TAG", videoUri.toString());
        Intent intent;
        switch (resourceId)
        {
            case R.id.trailer_id:

                intent = new Intent(Intent.ACTION_VIEW, videoUri);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
              //  intent.setType("video/*");
                if (mContext.getPackageManager() != null) {
                    mContext.startActivity(intent);
                }

                break;

            case R.id.share_video :
               videoUri = Uri.parse(ConstantUtil.SHARE_TRAILER).buildUpon().appendPath(videoKey).build();

                videoUrl = videoUri.toString();
                //Log.i("LOG_TAG", "share button clicked"+videoUrl);
                intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Watch "+mMovieParcelableObject.original_title +" - "+videosParcelableObject.name);
                intent.putExtra(Intent.EXTRA_TEXT,videoUrl);
                if (mContext.getPackageManager() != null) {
                    //mContext.startActivity(Intent.createChooser(intent,"Share using"));
                    mContext.startActivity(intent);
                }

                break;
        }
    }
}
