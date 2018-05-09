package com.scheffer.erik.videogamewishlist.converters;

import android.content.ContentValues;
import android.database.Cursor;

import com.scheffer.erik.videogamewishlist.database.WishlistContract;
import com.scheffer.erik.videogamewishlist.models.Genre;

public class GenreConverter {
    public static ContentValues toContentValues(Genre genre) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WishlistContract.GenreEntry.COLUMN_EXTERNAL_ID, genre.getId());
        contentValues.put(WishlistContract.GenreEntry.COLUMN_NAME, genre.getName());
        return contentValues;
    }

    public static Genre fromCursor(Cursor cursor) {
        return new Genre(cursor.getLong(cursor.getColumnIndex(WishlistContract.GenreEntry.COLUMN_EXTERNAL_ID)),
                         cursor.getString(cursor.getColumnIndex(WishlistContract.GenreEntry.COLUMN_NAME)));
    }
}
