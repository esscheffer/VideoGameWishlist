package com.scheffer.erik.videogamewishlist.converters;

import android.content.ContentValues;

import com.scheffer.erik.videogamewishlist.database.WishlistContract;
import com.scheffer.erik.videogamewishlist.models.Theme;

public class ThemeConverter {
    public static ContentValues toContentValues(Theme theme) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WishlistContract.ThemeEntry.COLUMN_EXTERNAL_ID, theme.getId());
        contentValues.put(WishlistContract.ThemeEntry.COLUMN_NAME, theme.getName());
        return contentValues;
    }
}
