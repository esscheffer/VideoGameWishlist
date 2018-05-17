package com.scheffer.erik.videogamewishlist.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Game(var id: Long = 0,
                var name: String = "",
                var summary: String? = null,
                var rating: Double = 0.toDouble(),
                var cover: Image? = null,
                var platforms: List<Platform>? = null,
                var genres: List<Genre>? = null,
                var themes: List<Theme>? = null,
                var videos: List<Video>? = null) : Parcelable