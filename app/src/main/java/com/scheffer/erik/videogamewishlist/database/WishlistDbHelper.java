package com.scheffer.erik.videogamewishlist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.scheffer.erik.videogamewishlist.database.WishlistContract.GameEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.GameGenreEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.GamePlatformEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.GameThemeEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.GenreEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.PlatformEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.ThemeEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.VideoEntry;

public class WishlistDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wishlist.db";
    private static final int DATABASE_VERSION = 1;

    public WishlistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_GAME_TABLE = "CREATE TABLE " +
                GameEntry.TABLE_NAME + "(" +
                GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GameEntry.COLUMN_EXTERNAL_ID + " INTEGER, " +
                GameEntry.COLUMN_NAME + " TEXT, " +
                GameEntry.COLUMN_SUMMARY + " TEXT, " +
                GameEntry.COLUMN_RATING + " REAL, " +
                GameEntry.COLUMN_COVER_URL + " TEXT, " +
                GameEntry.COLUMN_COVER_CLOUDINARY_ID + " TEXT, " +
                GameEntry.COLUMN_PLATFORMS + " TEXT, " +
                GameEntry.COLUMN_GENRES + " TEXT, " +
                GameEntry.COLUMN_THEMES + " TEXT, " +
                GameEntry.COLUMN_VIDEOS + " TEXT" +
                ");";

        final String CREATE_PLATFORM_TABLE = "CREATE TABLE " +
                PlatformEntry.TABLE_NAME + "(" +
                PlatformEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PlatformEntry.COLUMN_EXTERNAL_ID + " INTEGER UNIQUE, " +
                PlatformEntry.COLUMN_NAME + " TEXT" +
                ");";

        final String CREATE_GENRE_TABLE = "CREATE TABLE " +
                GenreEntry.TABLE_NAME + "(" +
                GenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GenreEntry.COLUMN_EXTERNAL_ID + " INTEGER UNIQUE, " +
                GenreEntry.COLUMN_NAME + " TEXT" +
                ");";

        final String CREATE_THEME_TABLE = "CREATE TABLE " +
                ThemeEntry.TABLE_NAME + "(" +
                ThemeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ThemeEntry.COLUMN_EXTERNAL_ID + " INTEGER UNIQUE, " +
                ThemeEntry.COLUMN_NAME + " TEXT" +
                ");";

        final String CREATE_VIDEO_TABLE = "CREATE TABLE " +
                VideoEntry.TABLE_NAME + "(" +
                VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VideoEntry.COLUMN_NAME + " TEXT, " +
                VideoEntry.COLUMN_VIDEO_ID + " TEXT, " +
                VideoEntry.COLUMN_GAME_ID + " INTEGER, " +
                "FOREIGN KEY (" + VideoEntry.COLUMN_GAME_ID + ") " +
                "REFERENCES " + GameEntry.TABLE_NAME + "(" + GameEntry._ID + ") ON DELETE CASCADE" +
                ");";

        final String CREATE_GAME_PLATFORM_TABLE = "CREATE TABLE " +
                GamePlatformEntry.TABLE_NAME + "(" +
                GamePlatformEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GamePlatformEntry.COLUMN_GAME_ID + " INTEGER, " +
                GamePlatformEntry.COLUMN_PLATFORM_EXTERNAL_ID + " INTEGER, " +
                "FOREIGN KEY (" + GamePlatformEntry.COLUMN_GAME_ID + ") " +
                "REFERENCES " + GameEntry.TABLE_NAME + "(" + GameEntry._ID + ") ON DELETE CASCADE," +
                "FOREIGN KEY (" + GamePlatformEntry.COLUMN_PLATFORM_EXTERNAL_ID + ") " +
                "REFERENCES " + PlatformEntry.TABLE_NAME + "(" + PlatformEntry.COLUMN_EXTERNAL_ID + ") ON DELETE CASCADE" +
                ");";

        final String CREATE_GAME_GENRE_TABLE = "CREATE TABLE " +
                GameGenreEntry.TABLE_NAME + "(" +
                GameGenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GameGenreEntry.COLUMN_GAME_ID + " INTEGER, " +
                GameGenreEntry.COLUMN_GENRE_EXTERNAL_ID + " INTEGER, " +
                "FOREIGN KEY (" + GameGenreEntry.COLUMN_GAME_ID + ") " +
                "REFERENCES " + GameEntry.TABLE_NAME + "(" + GameEntry._ID + ") ON DELETE CASCADE," +
                "FOREIGN KEY (" + GameGenreEntry.COLUMN_GENRE_EXTERNAL_ID + ") " +
                "REFERENCES " + GenreEntry.TABLE_NAME + "(" + GenreEntry.COLUMN_EXTERNAL_ID + ") ON DELETE CASCADE" +
                ");";

        final String CREATE_GAME_THEME_TABLE = "CREATE TABLE " +
                GameThemeEntry.TABLE_NAME + "(" +
                GameThemeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GameThemeEntry.COLUMN_GAME_ID + " INTEGER, " +
                GameThemeEntry.COLUMN_THEME_EXTERNAL_ID + " INTEGER, " +
                "FOREIGN KEY (" + GameThemeEntry.COLUMN_GAME_ID + ") " +
                "REFERENCES " + GameEntry.TABLE_NAME + "(" + GameEntry._ID + ") ON DELETE CASCADE," +
                "FOREIGN KEY (" + GameThemeEntry.COLUMN_THEME_EXTERNAL_ID + ") " +
                "REFERENCES " + ThemeEntry.TABLE_NAME + "(" + ThemeEntry.COLUMN_EXTERNAL_ID + ") ON DELETE CASCADE" +
                ");";

        db.execSQL(CREATE_GAME_TABLE);
        db.execSQL(CREATE_PLATFORM_TABLE);
        db.execSQL(CREATE_GENRE_TABLE);
        db.execSQL(CREATE_THEME_TABLE);
        db.execSQL(CREATE_VIDEO_TABLE);
        db.execSQL(CREATE_GAME_PLATFORM_TABLE);
        db.execSQL(CREATE_GAME_GENRE_TABLE);
        db.execSQL(CREATE_GAME_THEME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No upgrade. Database still in version 1
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onOpen(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
