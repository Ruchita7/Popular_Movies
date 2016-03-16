package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @author Ruchita_Maheshwary
 * MovieParcelableObject class implementing Parcelable interface for Popular Movies
 */
public class MovieParcelableObject implements Parcelable {


    String backdrop_path;
    String original_title;
    String poster_path;
    String release_date;
    String vote_average;
    String overview;
    int id;
    int vote_count;
    byte[] backgroundImage;
    byte[] posterImage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(original_title);
        dest.writeString(release_date);
        dest.writeString(vote_average);
        dest.writeString(overview);
        dest.writeString(backdrop_path);
        dest.writeInt(id);
        dest.writeInt(vote_count);
        dest.writeByteArray(backgroundImage);
        dest.writeByteArray(posterImage);
    }

    public MovieParcelableObject(String poster_path, String original_title,
                                 String release_date, String vote_average,
                                 String overview, String backdrop_path, int id, int vote_count, byte[] backgroundImage, byte[] posterImage) {
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.overview = overview;
        this.backdrop_path = backdrop_path;
        this.id = id;
        this.vote_count = vote_count;
        this.backgroundImage = backgroundImage;
        this.posterImage = posterImage;

    }

    private MovieParcelableObject(Parcel in) {
        poster_path = in.readString();
        original_title = in.readString();
        release_date = in.readString();
        vote_average = in.readString();
        overview = in.readString();
        backdrop_path = in.readString();
        id = in.readInt();
        vote_count = in.readInt();
        in.readByteArray(backgroundImage);
        in.readByteArray(posterImage);
    }

    public static final Parcelable.Creator<MovieParcelableObject> CREATOR = new Parcelable.Creator<MovieParcelableObject>() {
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
