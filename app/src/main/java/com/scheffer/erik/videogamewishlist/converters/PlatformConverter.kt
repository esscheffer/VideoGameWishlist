package com.scheffer.erik.videogamewishlist.converters

import com.scheffer.erik.videogamewishlist.models.Platform

fun fromProtoPlatformToPlatform(platform: proto.Platform) =
        Platform(platform.id, platform.name)