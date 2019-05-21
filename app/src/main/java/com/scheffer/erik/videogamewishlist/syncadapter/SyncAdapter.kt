package com.scheffer.erik.videogamewishlist.syncadapter

import APICalypse
import IGDBWrapper
import RequestException
import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.preference.PreferenceManager
import com.scheffer.erik.videogamewishlist.BuildConfig
import com.scheffer.erik.videogamewishlist.converters.fromProtoGenreToGenre
import com.scheffer.erik.videogamewishlist.converters.fromProtoPlatformToPlatform
import com.scheffer.erik.videogamewishlist.converters.fromProtoThemeToTheme
import com.scheffer.erik.videogamewishlist.utils.fastSaveAll
import genres
import platforms
import themes

private const val QUERY_CHUNK_SIZE = 10

class SyncAdapter : AbstractThreadedSyncAdapter {

    constructor(context: Context, autoInitialize: Boolean) : super(context, autoInitialize)

    constructor(context: Context,
                autoInitialize: Boolean,
                allowParallelSyncs: Boolean) : super(context, autoInitialize, allowParallelSyncs)

    override fun onPerformSync(account: Account,
                               extras: Bundle,
                               authority: String,
                               provider: ContentProviderClient,
                               syncResult: SyncResult) {
        IGDBWrapper.userkey = BuildConfig.API_KEY

        val params = APICalypse()
                .fields("*")
                .limit(50)

        try {
            IGDBWrapper.genres(params)
                    .map(::fromProtoGenreToGenre)
                    .fastSaveAll()

            saveSyncStatusToPreferences(PREF_GENRES_SYNC)
        } catch (e: RequestException) {
            e.printStackTrace()
        }

        try {
            IGDBWrapper.themes(params)
                    .map(::fromProtoThemeToTheme)
                    .fastSaveAll()

            saveSyncStatusToPreferences(PREF_THEMES_SYNC)
        } catch (e: RequestException) {
            e.printStackTrace()
        }

        /*
        The IGDB contains more than 150 platforms. Many are not interesting for this app
        target users. The following list is the ids of the desired platforms manually selected
        from the full list
        */
        val popularPlatformsIds = listOf("11", // Xbox
                "137",  //New Nintendo 3DS
                "6",    //PC (Microsoft Windows)
                "37",   //Nintendo 3DS
                "48",   //PlayStation 4
                "14",   //Mac
                "20",   //Nintendo DS
                "34",   //Android
                "9",    //PlayStation 3
                "41",   //Wii U
                "130",  //Nintendo Switch
                "39",   //iOS
                "3",    //Linux
                "38",   //PlayStation Portable (PSP)
                "12",   //Xbox 360
                "46",   //PlayStation Vita (PS Vita)
                "49")   //Xbox One

        //"38," +     //Xbox Live Arcade
        //"45," +     //PlayStation Network

        try {
            // My free plan limit the query by 10 IDs
            popularPlatformsIds.chunked(QUERY_CHUNK_SIZE).forEach {
                val platformsParams = APICalypse()
                        .fields("*")
                        .limit(50)
                        .where("id = (${it.joinToString()})")
                IGDBWrapper.platforms(platformsParams)
                        .map(::fromProtoPlatformToPlatform)
                        .fastSaveAll()
            }

            saveSyncStatusToPreferences(PREF_PLATFORMS_SYNC)
        } catch (e: RequestException) {
            e.printStackTrace()
        }
    }

    private fun saveSyncStatusToPreferences(key: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(key, true).apply()
    }

    companion object {
        const val PREF_PLATFORMS_SYNC = "platforms_sync"
        const val PREF_GENRES_SYNC = "genres_sync"
        const val PREF_THEMES_SYNC = "themes_sync"
    }
}
