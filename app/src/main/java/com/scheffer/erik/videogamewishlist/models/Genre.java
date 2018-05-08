package com.scheffer.erik.videogamewishlist.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Genre implements Parcelable {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
    }

    public Genre() {}

    protected Genre(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Genre> CREATOR = new Parcelable.Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel source) {return new Genre(source);}

        @Override
        public Genre[] newArray(int size) {return new Genre[size];}
    };
}
