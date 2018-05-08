package com.scheffer.erik.videogamewishlist.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    private String url;
    private String cloudinaryId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCloudinaryId() {
        return cloudinaryId;
    }

    public void setCloudinaryId(String cloudinaryId) {
        this.cloudinaryId = cloudinaryId;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.cloudinaryId);
    }

    public Image() {}

    protected Image(Parcel in) {
        this.url = in.readString();
        this.cloudinaryId = in.readString();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {return new Image(source);}

        @Override
        public Image[] newArray(int size) {return new Image[size];}
    };
}
