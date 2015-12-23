package com.example.android.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.android.popularmovies.R;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Utility class.
 */
public class MoviesUtility {

    public static final String LOG_TAG = MoviesUtility.class.getSimpleName();

    /**
     * Get sorted order from shared preferences
     *
     * @param context
     * @return
     */
    public static Integer getPreferredSortOrder(Context context) {
        int chosenOrder;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs != null) {
            String order = prefs.getString(context.getString(R.string.sorting_order), context.getString(R.string.pref_defaultValue));
            int order_value = Integer.parseInt(order);
            if (order_value >= 0) {
                Resources resources = context.getResources();
                chosenOrder = Integer.parseInt(resources.getStringArray(R.array.pref_sorting_values)[order_value]);
            } else {
                chosenOrder = order_value;
            }
        } else {
            chosenOrder = Integer.parseInt(context.getString(R.string.pref_defaultValue));
        }
        return chosenOrder;
    }

    /**
     * Format date to string
     *
     * @param date
     * @return
     */
    public static String formatDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        String movieReleaseDate = null;
        try {

            Date parsedDate = format.parse(date);
            DateFormat mediumDf = DateFormat.getDateInstance(DateFormat.MEDIUM);
            movieReleaseDate = mediumDf.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return movieReleaseDate;
    }

    /**
     * Check network connectivity
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * Get dimensions of listview
     *
     * @param myListView
     * @param additionalHeight
     */
    public static void getListViewSize(ListView myListView, int additionalHeight) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(myListView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1)) + additionalHeight;
        myListView.setLayoutParams(params);
        myListView.requestLayout();
    }

    /**
     * Get bytes of image input stream
     *
     * @param is
     * @return
     */
    public static byte[] getMovieImageInBytes(InputStream is) {
        try {

            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayBuffer baf = new ByteArrayBuffer(1024);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            return baf.toByteArray();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error: " + e.toString());
            return null;
        }

    }

    /**
     * @param image
     * @param bitmapWidth
     * @param bitmapHeight
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap image, int bitmapWidth,
                                          int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight,
                true);
    }
}
