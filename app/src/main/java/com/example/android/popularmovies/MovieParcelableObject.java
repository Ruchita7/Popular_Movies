package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 11/7/2015.
 */
public class MovieParcelableObject implements Parcelable {
    String posterUrl;
    String title;
    String releaseDate;
    String userRating;
    String synopsis;
    String backgroundUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(userRating);
        dest.writeString(synopsis);
        dest.writeString(backgroundUrl);
    }

    public MovieParcelableObject(String posterUrl, String title, String releaseDate, String userRating, String synopsis, String backgroundUrl)
    {
        this.posterUrl = posterUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this. userRating = userRating;
        this.synopsis = synopsis;
        this.backgroundUrl = backgroundUrl;
    }

    private MovieParcelableObject(Parcel in) {
        posterUrl = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        userRating = in.readString();
        synopsis = in.readString();
        backgroundUrl = in.readString();
    }

    public static final Parcelable.Creator<MovieParcelableObject> CREATOR = new Parcelable.Creator<MovieParcelableObject>()
    {
        @Override
        public MovieParcelableObject createFromParcel(Parcel source) {
            return new MovieParcelableObject(source);
        }

        @Override
        public MovieParcelableObject[] newArray(int size) {
            return new MovieParcelableObject[size];
        }
    };
}
