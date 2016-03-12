package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable Class for reviews
 */
public class ReviewsParcelableObject implements Parcelable {
    String id;
    String author;
    String content;
    String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public ReviewsParcelableObject(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    private ReviewsParcelableObject(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<ReviewsParcelableObject> CREATOR = new Parcelable.Creator<ReviewsParcelableObject>() {
        @Override
        public ReviewsParcelableObject createFromParcel(Parcel source) {
            return new ReviewsParcelableObject(source);
        }

        @Override
        public ReviewsParcelableObject[] newArray(int size) {
            return new ReviewsParcelableObject[size];
        }
    };
}
