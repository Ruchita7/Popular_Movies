package com.example.android.popularmovies.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Utility for setting layout of background image
 */
public class ImageUtil extends ImageView {

    /**
     * @param context
     * @param attrs
     */
    public ImageUtil(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final Drawable d = this.getDrawable();

        if (d != null) {
            // ceil not round - avoid thin vertical gaps along the left/right edges
            final int width = MeasureSpec.getSize(widthMeasureSpec);

            final int height = width * d.getIntrinsicHeight() / d.getIntrinsicWidth();
            this.setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
