package com.scheffer.erik.videogamewishlist.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Game(@PrimaryKey var id: Long = 0,
                var name: String = "",
                var summary: String? = null,
                var rating: Double = 0.toDouble(),
                var cover: Image? = null,
                var platforms: RealmList<Platform>? = RealmList(),
                var genres: RealmList<Genre>? = RealmList(),
                var themes: RealmList<Theme>? = RealmList(),
                var videos: RealmList<Video>? = RealmList()) : RealmObject()