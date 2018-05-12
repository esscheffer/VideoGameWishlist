package com.scheffer.erik.videogamewishlist.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Game implements Parcelable {
    private long id; // external API id
    private String name;
    private String summary;
    private double rating;
    private Image cover;
    private List<Platform> platforms;
    private List<Genre> genres;
    private List<Theme> themes;
    private List<Video> videos;

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Image getCover() {
        return cover;
    }

    public void setCover(Image cover) {
        this.cover = cover;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.summary);
        dest.writeDouble(this.rating);
        dest.writeParcelable(this.cover, flags);
        dest.writeTypedList(this.platforms);
        dest.writeTypedList(this.genres);
        dest.writeTypedList(this.themes);
        dest.writeTypedList(this.videos);
    }

    public Game() {}

    protected Game(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.summary = in.readString();
        this.rating = in.readDouble();
        this.cover = in.readParcelable(Image.class.getClassLoader());
        this.platforms = in.createTypedArrayList(Platform.CREATOR);
        this.genres = in.createTypedArrayList(Genre.CREATOR);
        this.themes = in.createTypedArrayList(Theme.CREATOR);
        this.videos = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {return new Game(source);}

        @Override
        public Game[] newArray(int size) {return new Game[size];}
    };
}
