package com.scheffer.erik.videogamewishlist.converters

import com.scheffer.erik.videogamewishlist.models.Theme

fun fromProtoThemeToTheme(theme: proto.Theme) =
        Theme(theme.id, theme.name)