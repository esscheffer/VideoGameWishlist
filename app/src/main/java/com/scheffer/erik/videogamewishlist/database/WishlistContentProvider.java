package com.scheffer.erik.videogamewishlist.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.scheffer.erik.videogamewishlist.database.WishlistContract.GameEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.GenreEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.PlatformEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.ThemeEntry;

// TODO: change ContentProvider for a better way to access DB after Udacity project deliver
public class WishlistContentProvider extends ContentProvider {
    private WishlistDbHelper dbHelper;

    public static final int GAMES = 100;
    public static final int GAMES_WITH_ID = 101;
    public static final int PLATFORMS = 102;
    public static final int PLATFORMS_WITH_ID = 103;
    public static final int GENRES = 104;
    public static final int GENRES_WITH_ID = 105;
    public static final int THEMES = 106;
    public static final int THEMES_WITH_ID = 107;
    public static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(WishlistContract.AUTHORITY,
                          WishlistContract.PATH_GAMES,
                          GAMES);
        uriMatcher.addURI(WishlistContract.AUTHORITY,
                          WishlistContract.PATH_GAMES + "/#",
                          GAMES_WITH_ID);

        uriMatcher.addURI(WishlistContract.AUTHORITY,
                          WishlistContract.PATH_PLATFORMS,
                          PLATFORMS);
        uriMatcher.addURI(WishlistContract.AUTHORITY,
                          WishlistContract.PATH_PLATFORMS + "/#",
                          PLATFORMS_WITH_ID);

        uriMatcher.addURI(WishlistContract.AUTHORITY,
                          WishlistContract.PATH_GENRES,
                          GENRES);
        uriMatcher.addURI(WishlistContract.AUTHORITY,
                          WishlistContract.PATH_GENRES + "/#",
                          GENRES_WITH_ID);

        uriMatcher.addURI(WishlistContract.AUTHORITY,
                          WishlistContract.PATH_THEMES,
                          THEMES);
        uriMatcher.addURI(WishlistContract.AUTHORITY,
                          WishlistContract.PATH_THEMES + "/#",
                          THEMES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int favoritesDeleted;
        switch (match) {
            case GAMES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                favoritesDeleted = database.delete(GameEntry.TABLE_NAME,
                                                   GameEntry._ID + "=?",
                                                   new String[]{String.valueOf(id)});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (favoritesDeleted != 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
        }

        return favoritesDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        Uri returnUri = null;
        long id;
        switch (uriMatcher.match(uri)) {
            case GAMES:
                id = database.insert(GameEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(GameEntry.CONTENT_URI, id);
                }
                break;
            case PLATFORMS:
                id = database.insert(PlatformEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(PlatformEntry.CONTENT_URI, id);
                }
                break;
            case THEMES:
                id = database.insert(ThemeEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(ThemeEntry.CONTENT_URI, id);
                }
                break;
            case GENRES:
                id = database.insert(GenreEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(GenreEntry.CONTENT_URI, id);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (id <= 0) {
            throw new SQLException("Failed to insert row into " + uri);
        }

        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new WishlistDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        Cursor returnCursor;
        switch (uriMatcher.match(uri)) {
            case GAMES:
                returnCursor = database.query(GameEntry.TABLE_NAME,
                                              null,
                                              null,
                                              null,
                                              null,
                                              null,
                                              sortOrder);
                break;
            case GAMES_WITH_ID:
                returnCursor = database.query(GameEntry.TABLE_NAME,
                                              null,
                                              GameEntry.COLUMN_EXTERNAL_ID + "=?",
                                              new String[]{uri.getPathSegments().get(1)},
                                              null,
                                              null,
                                              sortOrder);
                break;

            case PLATFORMS:
                returnCursor = database.query(PlatformEntry.TABLE_NAME,
                                              null,
                                              null,
                                              null,
                                              null,
                                              null,
                                              sortOrder);
                break;
            case PLATFORMS_WITH_ID:
                returnCursor = database.query(PlatformEntry.TABLE_NAME,
                                              null,
                                              PlatformEntry.COLUMN_EXTERNAL_ID + "=?",
                                              new String[]{uri.getPathSegments().get(1)},
                                              null,
                                              null,
                                              sortOrder);
                break;

            case GENRES:
                returnCursor = database.query(GenreEntry.TABLE_NAME,
                                              null,
                                              null,
                                              null,
                                              null,
                                              null,
                                              sortOrder);
                break;
            case GENRES_WITH_ID:
                returnCursor = database.query(GenreEntry.TABLE_NAME,
                                              null,
                                              GenreEntry.COLUMN_EXTERNAL_ID + "=?",
                                              new String[]{uri.getPathSegments().get(1)},
                                              null,
                                              null,
                                              sortOrder);
                break;

            case THEMES:
                returnCursor = database.query(ThemeEntry.TABLE_NAME,
                                              null,
                                              null,
                                              null,
                                              null,
                                              null,
                                              sortOrder);
                break;
            case THEMES_WITH_ID:
                returnCursor = database.query(ThemeEntry.TABLE_NAME,
                                              null,
                                              ThemeEntry.COLUMN_EXTERNAL_ID + "=?",
                                              new String[]{uri.getPathSegments().get(1)},
                                              null,
                                              null,
                                              sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnCursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int updateId;
        String id = uri.getPathSegments().get(1);
        switch (match) {
            case PLATFORMS_WITH_ID:
                updateId = database.update(PlatformEntry.TABLE_NAME,
                                           values,
                                           PlatformEntry._ID + "=?",
                                           new String[]{String.valueOf(id)});
                break;
            case GENRES_WITH_ID:
                updateId = database.update(GenreEntry.TABLE_NAME,
                                           values,
                                           GenreEntry._ID + "=?",
                                           new String[]{String.valueOf(id)});
                break;
            case THEMES_WITH_ID:
                updateId = database.update(ThemeEntry.TABLE_NAME,
                                           values,
                                           ThemeEntry._ID + "=?",
                                           new String[]{String.valueOf(id)});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (updateId != 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
        }

        return updateId;
    }
}
