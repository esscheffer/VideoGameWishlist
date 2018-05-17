package com.scheffer.erik.videogamewishlist.models

data class IGDBGame(var id: Long = 0, // external API id
                    var name: String = "",
                    var summary: String? = null,
                    var rating: Double = 0.toDouble(),
                    var cover: Image? = null,
                    var platforms: List<Long>? = null,
                    var genres: List<Long>? = null,
                    var themes: List<Long>? = null,
                    var videos: List<Video>? = null)
