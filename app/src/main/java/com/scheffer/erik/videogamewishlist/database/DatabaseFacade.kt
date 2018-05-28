package com.scheffer.erik.videogamewishlist.database

import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.scheffer.erik.videogamewishlist.database.databasemodels.GameDBModel
import com.scheffer.erik.videogamewishlist.database.databasemodels.GameDBModel_Table
import com.scheffer.erik.videogamewishlist.models.*

fun getAllGames(): MutableList<GameDBModel> = SQLite.select()
        .from<GameDBModel>()
        .orderBy(GameDBModel_Table.rating, false)
        .queryList()

fun getAllPlatforms(): MutableList<Platform> = SQLite.select()
        .from<Platform>()
        .orderBy(Platform_Table.name, true)
        .queryList()

fun getAllGenres(): MutableList<Genre> = SQLite.select()
        .from<Genre>()
        .orderBy(Genre_Table.name, true)
        .queryList()

fun getAllThemes(): MutableList<Theme> = SQLite.select()
        .from<Theme>()
        .orderBy(Theme_Table.name, true)
        .queryList()

fun getPlatformsByIdList(ids: List<Long>): MutableList<Platform> =
        (select from Platform::class
                where (Platform_Table.id `in` ids)).list

fun getGenresByIdList(ids: List<Long>): MutableList<Genre> =
        (select from Genre::class
                where (Genre_Table.id `in` ids)).list

fun getThemesByIdList(ids: List<Long>): MutableList<Theme> =
        (select from Theme::class
                where (Theme_Table.id `in` ids)).list

fun getGameById(id: Long) =
        (select from GameDBModel::class
                where (GameDBModel_Table.id eq id)).querySingle()