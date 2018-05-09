package com.scheffer.erik.videogamewishlist.converters;

import android.content.ContentValues;
import android.database.Cursor;

import com.scheffer.erik.videogamewishlist.database.WishlistContract.PlatformEntry;
import com.scheffer.erik.videogamewishlist.models.Platform;

public class PlatformConverter {
    public static ContentValues toContentValues(Platform platform) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlatformEntry.COLUMN_EXTERNAL_ID, platform.getId());
        contentValues.put(PlatformEntry.COLUMN_NAME, platform.getName());
        return contentValues;
    }

    public static Platform fromCursor(Cursor cursor) {
        return new Platform(cursor.getLong(cursor.getColumnIndex(PlatformEntry.COLUMN_EXTERNAL_ID)),
                            cursor.getString(cursor.getColumnIndex(PlatformEntry.COLUMN_NAME)));
    }
}
