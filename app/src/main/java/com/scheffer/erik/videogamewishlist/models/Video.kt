package com.scheffer.erik.videogamewishlist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(var name: String = "", var videoId: String = "") : Parcelable