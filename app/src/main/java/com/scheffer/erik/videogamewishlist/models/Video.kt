package com.scheffer.erik.videogamewishlist.models

import android.os.Parcelable
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import com.scheffer.erik.videogamewishlist.database.AppDatabase
import com.scheffer.erik.videogamewishlist.database.databasemodels.GameDBModel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Table(database = AppDatabase::class, allFields = true)
data class Video(var name: String = "",
                 @PrimaryKey var videoId: String = "",
                 @ForeignKey(tableClass = GameDBModel::class, onDelete = ForeignKeyAction.CASCADE) var gameId: Long? = null)
    : Parcelable, BaseModel()