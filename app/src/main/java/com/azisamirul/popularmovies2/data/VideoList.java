package com.azisamirul.popularmovies2.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by azisamirul on 07/08/2017.
 */

public class VideoList implements Parcelable{
    public String key;
    public String name;
    public String site;
    public String type;


    public VideoList(){

    }
    protected VideoList(Parcel in){
        key=in.readString();
        name=in.readString();
        site=in.readString();
        type=in.readString();
    }

    public static final Creator CREATOR=new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new VideoList(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new VideoList[size];
        }
    };

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(site);
    }
}
