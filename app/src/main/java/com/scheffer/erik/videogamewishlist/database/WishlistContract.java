package com.scheffer.erik.videogamewishlist.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class WishlistContract {

    public static final String AUTHORITY = "com.scheffer.erik.videogamewishlist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_GAMES = "games";
    public static final String PATH_PLATFORMS = "platforms";
    public static final String PATH_GENRES = "genres";
    public static final String PATH_THEMES = "themes";
    public static final String PATH_VIDEOS = "videos";
    public static final String PATH_GAMES_PLATFORMS = "games-platforms";
    public static final String PATH_GAMES_GENRES = "games-genres";
    public static final String PATH_GAMES_THEMES = "games-themes";
    public static final String PATH_RANDOM = "random";

    public static final class GameEntry implements BaseColumns {

        public static final Uri CONTENT_URI = getContentUri(PATH_GAMES);

        public static final String TABLE_NAME = "game";
        public static final String COLUMN_EXTERNAL_ID = "externalId";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_RATING = "rating";

        /*
         following columns are used to save a list of this entities as string to avoid
         the need to make a JOIN and will be changed after project deliver when the
         content provider will be replaced by a better DB library
        */

        public static final String COLUMN_COVER_URL = "coverUrl";
        public static final String COLUMN_COVER_CLOUDINARY_ID = "cloudinaryId";

        public static final String COLUMN_PLATFORMS = "platforms";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_THEMES = "themes";
        public static final String COLUMN_VIDEOS = "videos";
    }

    public static final class PlatformEntry implements BaseColumns {

        public static final Uri CONTENT_URI = getContentUri(PATH_PLATFORMS);

        public static final String TABLE_NAME = "platform";
        public static final String COLUMN_EXTERNAL_ID = "externalId";
        public static final String COLUMN_NAME = "name";
    }

    public static final class GenreEntry implements BaseColumns {

        public static final Uri CONTENT_URI = getContentUri(PATH_GENRES);

        public static final String TABLE_NAME = "genre";
        public static final String COLUMN_EXTERNAL_ID = "externalId";
        public static final String COLUMN_NAME = "name";
    }

    public static final class ThemeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = getContentUri(PATH_THEMES);

        public static final String TABLE_NAME = "theme";
        public static final String COLUMN_EXTERNAL_ID = "externalId";
        public static final String COLUMN_NAME = "name";
    }

    public static final class VideoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = getContentUri(PATH_VIDEOS);

        public static final String TABLE_NAME = "video";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_VIDEO_ID = "videoId";
        public static final String COLUMN_GAME_ID = "gameId";
    }

    public static final class GamePlatformEntry implements BaseColumns {

        public static final Uri CONTENT_URI = getContentUri(PATH_GAMES_PLATFORMS);

        public static final String TABLE_NAME = "gamePlatform";
        public static final String COLUMN_GAME_ID = "gameId";
        public static final String COLUMN_PLATFORM_EXTERNAL_ID = "platformExternalId";
    }

    public static final class GameGenreEntry implements BaseColumns {

        public static final Uri CONTENT_URI = getContentUri(PATH_GAMES_GENRES);

        public static final String TABLE_NAME = "gameGenre";
        public static final String COLUMN_GAME_ID = "gameId";
        public static final String COLUMN_GENRE_EXTERNAL_ID = "genreExternalId";
    }

    public static final class GameThemeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = getContentUri(PATH_GAMES_THEMES);

        public static final String TABLE_NAME = "gameTheme";
        public static final String COLUMN_GAME_ID = "gameId";
        public static final String COLUMN_THEME_EXTERNAL_ID = "themeExternalId";
    }

    private static Uri getContentUri(String path) {
        return BASE_CONTENT_URI.buildUpon().appendPath(path).build();
    }
}
