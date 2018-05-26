package com.scheffer.erik.videogamewishlist.utils;

import com.scheffer.erik.videogamewishlist.models.Game;
import com.scheffer.erik.videogamewishlist.models.Genre;
import com.scheffer.erik.videogamewishlist.models.Image;
import com.scheffer.erik.videogamewishlist.models.Platform;
import com.scheffer.erik.videogamewishlist.models.Theme;
import com.scheffer.erik.videogamewishlist.models.Video;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static Game getMockGame() {
        Game game = new Game();
        game.setName("very long game title to see if the text get wrapped to a new line");
        game.setSummary("summary");
        game.setRating(5.5);

        Image cover = new Image();
        cover.setUrl("https://images.igdb.com/igdb/image/upload/t_thumb/dp2l6fpd1yfj0sxcjsi8.jpg");
        cover.setCloudinaryId("dp2l6fpd1yfj0sxcjsi8");
        game.setCover(cover);

        Platform p1 = new Platform();
        p1.setId(1);
        p1.setName("p1");
        Platform p2 = new Platform();
        p2.setId(2);
        p2.setName("p2");
        List<Platform> platforms = new ArrayList<>();
        platforms.add(p1);
        platforms.add(p2);
//        game.setPlatforms(platforms);

        Genre g1 = new Genre();
        g1.setId(1);
        g1.setName("g1");
        Genre g2 = new Genre();
        g2.setId(2);
        g2.setName("g2");
        List<Genre> genres = new ArrayList<>();
        genres.add(g1);
        genres.add(g2);
//        game.setGenres(genres);

        Theme t1 = new Theme();
        t1.setId(1);
        t1.setName("t1");
        Theme t2 = new Theme();
        t2.setId(2);
        t2.setName("t2");
        List<Theme> themes = new ArrayList<>();
        themes.add(t1);
        themes.add(t2);
//        game.setThemes(themes);

        Video v1 = new Video();
        v1.setName("v1");
        v1.setVideoId("id1");
        Video v2 = new Video();
        v2.setName("v2");
        v2.setVideoId("id2");
        List<Video> videos = new ArrayList<>();
        videos.add(v1);
        videos.add(v2);
//        game.setVideos(videos);

        return game;
    }
}
