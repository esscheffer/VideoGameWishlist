package com.scheffer.erik.videogamewishlist.utils;

import android.support.annotation.StringDef;

import com.scheffer.erik.videogamewishlist.models.Image;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class IGDBImageUtils {
    // image sizes
    public static final String COVER_SMALL = "cover_small";
    public static final String SCREENSHOT_MED = "screenshot_med";
    public static final String COVER_BIG = "cover_big";
    public static final String LOGO_MED = "logo_med";
    public static final String SCREENSHOT_BIG = "screenshot_big";
    public static final String SCREENSHOT_HUGE = "screenshot_huge";
    public static final String THUMB = "thumb";
    public static final String MICRO = "micro";
    public static final String SIZE_720P = "720p";
    public static final String SIZE_1080P = "1080p";

    // First parameter is size, second is hash
    private static final String baseImageUrl = "https://images.igdb.com/igdb/image/upload/t_%s/%s.jpg";

    @Retention(SOURCE)
    @StringDef({
            COVER_SMALL,
            SCREENSHOT_MED,
            COVER_BIG,
            LOGO_MED,
            SCREENSHOT_BIG,
            SCREENSHOT_HUGE,
            THUMB,
            MICRO,
            SIZE_720P,
            SIZE_1080P,
    })
    public @interface ImageSize {}

    public static String getImageUrl(Image image, @ImageSize String imageSize) {
        return String.format(baseImageUrl, imageSize, image.getCloudinaryId());
    }
}
