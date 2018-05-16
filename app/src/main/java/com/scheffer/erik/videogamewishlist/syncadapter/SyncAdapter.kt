package com.scheffer.erik.videogamewishlist.syncadapter

import android.accounts.Account
import android.content.*
import android.os.Bundle
import android.preference.PreferenceManager
import callback.OnSuccessCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scheffer.erik.videogamewishlist.BuildConfig
import com.scheffer.erik.videogamewishlist.converters.GenreConverter
import com.scheffer.erik.videogamewishlist.converters.PlatformConverter
import com.scheffer.erik.videogamewishlist.converters.ThemeConverter
import com.scheffer.erik.videogamewishlist.database.WishlistContract
import com.scheffer.erik.videogamewishlist.models.Genre
import com.scheffer.erik.videogamewishlist.models.Platform
import com.scheffer.erik.videogamewishlist.models.Theme
import org.json.JSONArray
import wrapper.IGDBWrapper
import wrapper.Parameters
import wrapper.Version

class SyncAdapter : AbstractThreadedSyncAdapter {

    private var contentResolver: ContentResolver

    constructor(context: Context, autoInitialize: Boolean) : super(context, autoInitialize) {
        contentResolver = context.contentResolver
    }

    constructor(context: Context,
                autoInitialize: Boolean,
                allowParallelSyncs: Boolean) : super(context, autoInitialize, allowParallelSyncs) {
        contentResolver = context.contentResolver
    }

    override fun onPerformSync(account: Account,
                               extras: Bundle,
                               authority: String,
                               provider: ContentProviderClient,
                               syncResult: SyncResult) {
        val params = Parameters().addFields("*").addLimit("50")

        val wrapper = IGDBWrapper(BuildConfig.API_KEY, Version.STANDARD, false)

        wrapper.genres(params, object : OnSuccessCallback {
            override fun onSuccess(result: JSONArray) {
                val listType = object : TypeToken<List<Genre>>() {

                }.type
                val genres = Gson().fromJson<List<Genre>>(result.toString(), listType)

                for (genre in genres) {
                    contentResolver.insert(WishlistContract.GenreEntry.CONTENT_URI,
                                           GenreConverter.toContentValues(genre))
                }

                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putBoolean(PREF_GENRES_SYNC, true).apply()
            }

            override fun onError(error: Exception) {
                error.printStackTrace()
            }
        })

        wrapper.themes(params, object : OnSuccessCallback {
            override fun onSuccess(result: JSONArray) {
                val listType = object : TypeToken<List<Theme>>() {

                }.type
                val themes = Gson().fromJson<List<Theme>>(result.toString(), listType)

                for (theme in themes) {
                    contentResolver.insert(WishlistContract.ThemeEntry.CONTENT_URI,
                                           ThemeConverter.toContentValues(theme))
                }

                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putBoolean(PREF_THEMES_SYNC, true).apply()
            }

            override fun onError(error: Exception) {
                error.printStackTrace()
            }
        })

        /*
        The IGDB contains more than 150 platforms. Many are not interesting for this app
        target users. The following list is the ids of the desired platforms manually selected
        from the full list
        */
        val popularPlatformsIds = "11," +     // Xbox
                "137," +    //New Nintendo 3DS
                "6," +      //PC (Microsoft Windows)
                "37," +     //Nintendo 3DS
                "48," +     //PlayStation 4
                "17," +     //Mac
                "20," +     //Nintendo DS
                "34," +     //Android
                "9," +      //PlayStation 3
                "41," +     //Wii U
                "130," +    //Nintendo Switch
                "39," +     //iOS
                "3," +      //Linux
                "38," +     //PlayStation Portable
                "12," +     //Xbox 360
                "46," +     //PlayStation Vita
                "49"       //Xbox One

        //"38," +     //Xbox Live Arcade
        //"45," +     //PlayStation Network
        params.addIds(popularPlatformsIds)
        wrapper.platforms(params, object : OnSuccessCallback {
            override fun onSuccess(result: JSONArray) {
                val listType = object : TypeToken<List<Platform>>() {

                }.type
                val platforms = Gson().fromJson<List<Platform>>(result.toString(), listType)

                for (platform in platforms) {
                    contentResolver.insert(WishlistContract.PlatformEntry.CONTENT_URI,
                                           PlatformConverter.toContentValues(platform))
                }

                PreferenceManager.getDefaultSharedPreferences(context).edit()
                        .putBoolean(PREF_PLATFORMS_SYNC, true).apply()
            }

            override fun onError(error: Exception) {
                error.printStackTrace()
            }
        })
    }

    companion object {
        const val PREF_PLATFORMS_SYNC = "platforms_sync"
        const val PREF_GENRES_SYNC = "genres_sync"
        const val PREF_THEMES_SYNC = "themes_sync"
    }
}
