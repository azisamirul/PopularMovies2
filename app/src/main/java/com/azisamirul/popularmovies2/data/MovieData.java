package com.azisamirul.popularmovies2.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by azisamirul on 10/07/2017.
 */

public class MovieData implements Parcelable {
    public String id;
    public String originalTitle;
    public String posterPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String original_title) {
        this.originalTitle = original_title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    private MovieData(Parcel in){
        id=in.readString();
        originalTitle=in.readString();
        posterPath=in.readString();
    }
    public MovieData(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(posterPath);
        dest.writeString(originalTitle);

    }

    public static final Creator CREATOR=new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
           return new MovieData(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new MovieData[size];
        }
    };
}
