package com.scheffer.erik.videogamewishlist.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Theme implements Parcelable {
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

    public Theme() {}

    public Theme(long id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Theme(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Theme> CREATOR = new Parcelable.Creator<Theme>() {
        @Override
        public Theme createFromParcel(Parcel source) {return new Theme(source);}

        @Override
        public Theme[] newArray(int size) {return new Theme[size];}
    };
}
