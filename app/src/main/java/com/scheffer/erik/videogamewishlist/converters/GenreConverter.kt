package com.scheffer.erik.videogamewishlist.converters

import com.scheffer.erik.videogamewishlist.models.Genre

fun fromProtoGenreToGenre(genre: proto.Genre) =
        Genre(genre.id, genre.name)