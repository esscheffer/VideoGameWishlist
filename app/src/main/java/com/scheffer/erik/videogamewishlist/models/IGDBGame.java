package com.scheffer.erik.videogamewishlist.models;

import java.util.List;

public class IGDBGame {
    private long id; // external API id
    private String name;
    private String summary;
    private double rating;
    private Image cover;
    private List<Long> platforms;
    private List<Long> genres;
    private List<Long> themes;
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

    public List<Long> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Long> platforms) {
        this.platforms = platforms;
    }

    public List<Long> getGenres() {
        return genres;
    }

    public void setGenres(List<Long> genres) {
        this.genres = genres;
    }

    public List<Long> getThemes() {
        return themes;
    }

    public void setThemes(List<Long> themes) {
        this.themes = themes;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
