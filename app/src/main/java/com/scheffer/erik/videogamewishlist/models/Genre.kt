package com.scheffer.erik.videogamewishlist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(var id: Long = 0, var name: String = "") : Parcelable