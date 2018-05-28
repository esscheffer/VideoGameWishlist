package com.scheffer.erik.videogamewishlist.models

import android.os.Parcelable
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import com.scheffer.erik.videogamewishlist.database.AppDatabase
import kotlinx.android.parcel.Parcelize

@Parcelize
@Table(database = AppDatabase::class, allFields = true)
data class Genre(@PrimaryKey var id: Long = 0, var name: String = "") : Parcelable, BaseModel()