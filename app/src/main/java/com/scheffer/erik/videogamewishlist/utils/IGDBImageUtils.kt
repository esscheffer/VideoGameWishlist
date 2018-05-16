package com.scheffer.erik.videogamewishlist.utils

import android.support.annotation.StringDef
import com.scheffer.erik.videogamewishlist.models.Image

object IGDBImageUtils {
    // image sizes
    const val COVER_SMALL = "cover_small"
    const val SCREENSHOT_MED = "screenshot_med"
    const val COVER_BIG = "cover_big"
    const val LOGO_MED = "logo_med"
    const val SCREENSHOT_BIG = "screenshot_big"
    const val SCREENSHOT_HUGE = "screenshot_huge"
    const val THUMB = "thumb"
    const val MICRO = "micro"
    const val SIZE_720P = "720p"
    const val SIZE_1080P = "1080p"

    // First parameter is size, second is hash
    private const val baseImageUrl = "https://images.igdb.com/igdb/image/upload/t_%s/%s.jpg"

    @StringDef(COVER_SMALL,
               SCREENSHOT_MED,
               COVER_BIG,
               LOGO_MED,
               SCREENSHOT_BIG,
               SCREENSHOT_HUGE,
               THUMB,
               MICRO,
               SIZE_720P,
               SIZE_1080P)
    annotation class ImageSize

    @JvmStatic
    fun getImageUrl(image: Image, @ImageSize imageSize: String): String {
        return String.format(baseImageUrl, imageSize, image.cloudinaryId)
    }
}
