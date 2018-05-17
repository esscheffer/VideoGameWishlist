package com.scheffer.erik.videogamewishlist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(var url: String = "", var cloudinaryId: String = "") : Parcelable