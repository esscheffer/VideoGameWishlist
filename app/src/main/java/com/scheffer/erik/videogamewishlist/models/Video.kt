package com.scheffer.erik.videogamewishlist.models

import android.os.Parcelable
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize

//open class Video(@PrimaryKey var name: String = "", var videoId: String = "") : RealmObject()

@RealmClass
@Parcelize
open class Video(@PrimaryKey var name: String = "", var videoId: String = "") : RealmModel, Parcelable