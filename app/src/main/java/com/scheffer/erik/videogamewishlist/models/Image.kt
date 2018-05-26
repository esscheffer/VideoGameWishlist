package com.scheffer.erik.videogamewishlist.models

import android.os.Parcelable
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.android.parcel.Parcelize

//open class Image(@PrimaryKey var url: String = "", var cloudinaryId: String = "") : RealmObject()

@RealmClass
@Parcelize
open class Image(@PrimaryKey var url: String = "", var cloudinaryId: String = "") : RealmModel, Parcelable