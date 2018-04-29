package com.scheffer.erik.videogamewishlist.converters;

import android.content.ContentValues;

import com.scheffer.erik.videogamewishlist.database.WishlistContract.GameEntry;
import com.scheffer.erik.videogamewishlist.models.Game;
import com.scheffer.erik.videogamewishlist.models.Genre;
import com.scheffer.erik.videogamewishlist.models.Platform;
import com.scheffer.erik.videogamewishlist.models.Theme;
import com.scheffer.erik.videogamewishlist.models.Video;

public class GameConverter {
    public static ContentValues toContentValues(Game game) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(GameEntry.COLUMN_EXTERNAL_ID, game.getId());
        contentValues.put(GameEntry.COLUMN_NAME, game.getName());
        contentValues.put(GameEntry.COLUMN_SUMMARY, game.getSummary());
        contentValues.put(GameEntry.COLUMN_RATING, game.getRating());

        if (game.getCover() != null) {
            contentValues.put(GameEntry.COLUMN_COVER_URL, game.getCover().getUrl());
            contentValues.put(GameEntry.COLUMN_COVER_CLOUDINARY_ID, game.getCover().getCloudinaryId());
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
        for (Video video : game.getVideos()) {
            videosBuilder.append(video.getName()).append("#")
                         .append(video.getVideoId()).append(";");
        }
        contentValues.put(GameEntry.COLUMN_VIDEOS, videosBuilder.toString());

        return contentValues;
    }
}
