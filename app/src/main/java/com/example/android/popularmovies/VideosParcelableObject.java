package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Parcelable class for Videos
 */
public class VideosParcelableObject implements Parcelable {

    String id;
    String iso_639_1;
    String key;
    String name;
    String site;
    String size;
    String type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(size);
        dest.writeString(type);
    }

public  VideosParcelableObject(String id,String iso_639_1,String key,String name,String site,String size,String type)
{
    this.id=id;
    this.iso_639_1=iso_639_1;
    this.key=key;
    this.name=name;
    this.site=site;
    this.size=size;
    this.type=type;
}

    private VideosParcelableObject(Parcel in)
    {
        this.id = in.readString();
        this.iso_639_1=in.readString();
        this.key=in.readString();
        this.name=in.readString();
        this.site=in.readString();
        this.size=in.readString();
        this.type=in.readString();
    }

    public static final Parcelable.Creator<VideosParcelableObject> CREATOR = new Parcelable.Creator<VideosParcelableObject>() {
        @Override
        public VideosParcelableObject createFromParcel(Parcel source) {
            return new VideosParcelableObject(source);
        }

        @Override
        public VideosParcelableObject[] newArray(int size) {
            return new VideosParcelableObject[size];
        }
    };

}
