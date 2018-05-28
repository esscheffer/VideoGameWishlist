package com.scheffer.erik.videogamewishlist.database

import com.raizlabs.android.dbflow.annotation.ConflictAction
import com.raizlabs.android.dbflow.annotation.Database

@Database(name = AppDatabase.NAME,
        version = AppDatabase.VERSION,
        foreignKeyConstraintsEnforced = true,
        insertConflict = ConflictAction.REPLACE,
        updateConflict = ConflictAction.REPLACE)
object AppDatabase {
    const val NAME: String = "app"
    const val VERSION: Int = 1
}