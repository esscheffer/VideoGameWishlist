package com.scheffer.erik.videogamewishlist.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {
    private String name;
    private String videoId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.videoId);
    }

    public Video() {}

    private Video(Parcel in) {
        this.name = in.readString();
        this.videoId = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {return new Video(source);}

        @Override
        public Video[] newArray(int size) {return new Video[size];}
    };
}
