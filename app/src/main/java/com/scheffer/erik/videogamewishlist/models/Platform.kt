package com.scheffer.erik.videogamewishlist.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Platform(@PrimaryKey var id: Long = 0, var name: String = "") : RealmObject()

//@RealmClass
//@Parcelize
//open class Platform(@PrimaryKey var id: Long = 0, var name: String = "") : RealmModel, Parcelable