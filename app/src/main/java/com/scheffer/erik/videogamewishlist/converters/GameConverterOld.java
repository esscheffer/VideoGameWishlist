package com.scheffer.erik.videogamewishlist.converters;

import android.content.ContentValues;
import android.database.Cursor;

import com.scheffer.erik.videogamewishlist.database.WishlistContract.GameEntry;
import com.scheffer.erik.videogamewishlist.models.Game;
import com.scheffer.erik.videogamewishlist.models.Genre;
import com.scheffer.erik.videogamewishlist.models.Image;
import com.scheffer.erik.videogamewishlist.models.Platform;
import com.scheffer.erik.videogamewishlist.models.Theme;
import com.scheffer.erik.videogamewishlist.models.Video;

import java.util.ArrayList;
import java.util.List;

public class GameConverterOld {
    public static ContentValues toContentValues(Game game) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(GameEntry.COLUMN_EXTERNAL_ID, game.getId());
        contentValues.put(GameEntry.COLUMN_NAME, game.getName());
        contentValues.put(GameEntry.COLUMN_SUMMARY, game.getSummary());
        contentValues.put(GameEntry.COLUMN_RATING, game.getRating());

        if (game.getCover() != null) {
            contentValues.put(GameEntry.COLUMN_COVER_URL, game.getCover().getUrl());
            contentValues.put(GameEntry.COLUMN_COVER_CLOUDINARY_ID,
                              game.getCover().getCloudinaryId());
        }

        StringBuilder platformsBuilder = new StringBuilder();
        for (Platform platform : game.getPlatforms()) {
            platformsBuilder.append(platform.getName()).append(";");
        }
        contentValues.put(GameEntry.COLUMN_PLATFORMS, platformsBuilder.toString());

        StringBuilder genresBuilder = new StringBuilder();
        for (Genre genre : game.getGenres()) {
            genresBuilder.append(genre.getName()).append(";");
        }
        contentValues.put(GameEntry.COLUMN_GENRES, genresBuilder.toString());

        StringBuilder themesBuilder = new StringBuilder();
        for (Theme theme : game.getThemes()) {
            themesBuilder.append(theme.getName()).append(";");
        }
        contentValues.put(GameEntry.COLUMN_THEMES, themesBuilder.toString());

        StringBuilder videosBuilder = new StringBuilder();
        if (game.getVideos() != null) {
            for (Video video : game.getVideos()) {
                videosBuilder.append(video.getName()).append("#")
                             .append(video.getVideoId()).append(";");
            }
            contentValues.put(GameEntry.COLUMN_VIDEOS, videosBuilder.toString());
        }

        return contentValues;
    }

    public static Game fromCursor(Cursor cursor) {
        Game game = new Game();

        game.setName(cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_NAME)));
        game.setId(cursor.getLong(cursor.getColumnIndex(GameEntry.COLUMN_EXTERNAL_ID)));
        game.setSummary(cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_SUMMARY)));
        game.setRating(cursor.getDouble(cursor.getColumnIndex(GameEntry.COLUMN_RATING)));

        String coverCloudinaryId = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_COVER_CLOUDINARY_ID));
        if (coverCloudinaryId != null && !coverCloudinaryId.isEmpty()) {
            Image cover = new Image();
            cover.setCloudinaryId(coverCloudinaryId);
            game.setCover(cover);
        }

        List<Platform> platforms = new ArrayList<>();
        String platformsString = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_PLATFORMS));
        if (platformsString != null && !platformsString.isEmpty()) {
            String[] platformsSplit = platformsString.split(";");
            for (String platformName : platformsSplit) {
                Platform platform = new Platform();
                platform.setName(platformName);
                platforms.add(platform);
            }
        }
//        game.setPlatforms(platforms);

        List<Genre> genres = new ArrayList<>();
        String genresString = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_GENRES));
        if (genresString != null && !genresString.isEmpty()) {
            String[] genresSplit = genresString.split(";");
            for (String genreName : genresSplit) {
                Genre genre = new Genre();
                genre.setName(genreName);
                genres.add(genre);
            }
        }
//        game.setGenres(genres);

        List<Theme> themes = new ArrayList<>();
        String themesString = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_THEMES));
        if (themesString != null && !themesString.isEmpty()) {
            String[] themesSplit = themesString.split(";");
            for (String themeName : themesSplit) {
                Theme theme = new Theme();
                theme.setName(themeName);
                themes.add(theme);
            }
        }
//        game.setThemes(themes);

        List<Video> videos = new ArrayList<>();
        String videosString = cursor.getString(cursor.getColumnIndex(GameEntry.COLUMN_VIDEOS));
        if (videosString != null && !videosString.isEmpty()) {
            String[] videosSplit = videosString.split(";");
            for (String videoString : videosSplit) {
                String[] videoParts = videoString.split("#");
                Video video = new Video();
                video.setName(videoParts[0]);
                video.setVideoId(videoParts[1]);
                videos.add(video);
            }
        }
//        game.setVideos(videos);

        return game;
    }
}
