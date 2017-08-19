package com.azisamirul.popularmovies2.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by azisamirul on 09/08/2017.
 */

public class ReviewList implements Parcelable {
    public String author;
    public String content;
    public String movieTitle;
    public String id;

    public ReviewList(){

    }
    protected ReviewList(Parcel in){
        author=in.readString();
        id=in.readString();
        content=in.readString();
        movieTitle=in.readString();
    }


    public void setAuthor(String author) {
        this.author = author;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getContent() {
        return content;
    }

    public static final Creator CREATOR=new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
           return new ReviewList(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new ReviewList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(movieTitle);
    }
}
