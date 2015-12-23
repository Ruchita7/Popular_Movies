package com.example.android.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for handling reviews
 */
public class ReviewsAdapter extends ArrayAdapter<ReviewsParcelableObject> {

    private Activity mContext;

    private List<ReviewsParcelableObject> mReviewsList;
    private ReviewsParcelableObject mReviews;

    /**
     * @param context
     */
    public ReviewsAdapter(Activity context) {
        super(context, 0, new ArrayList<ReviewsParcelableObject>());
        mContext = context;
    }

    /**
     * @param context
     * @param reviewsList
     */
    ReviewsAdapter(Activity context, List<ReviewsParcelableObject> reviewsList) {
        super(context, 0, reviewsList);
        mContext = context;
        mReviewsList = reviewsList;
    }


    public static class ViewHolder {
        public final TextView reviewerTextView;
        public final TextView reviewTextView;

        public ViewHolder(View view) {
            reviewerTextView = (TextView) view.findViewById(R.id.reviewer_id);
            reviewTextView = (TextView) view.findViewById(R.id.review_detail);
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        View view = null;

        mReviews = mReviewsList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.movie_reviews_detail, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            viewHolder.reviewerTextView.setText(mReviews.author);
            viewHolder.reviewTextView.setText(mReviews.content);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.reviewerTextView.setText(mReviews.author);
            viewHolder.reviewTextView.setText(mReviews.content);

        }

        return convertView;
    }


}
