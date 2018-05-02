package com.scheffer.erik.videogamewishlist.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scheffer.erik.videogamewishlist.BuildConfig;
import com.scheffer.erik.videogamewishlist.converters.GenreConverter;
import com.scheffer.erik.videogamewishlist.converters.PlatformConverter;
import com.scheffer.erik.videogamewishlist.converters.ThemeConverter;
import com.scheffer.erik.videogamewishlist.database.WishlistContract;
import com.scheffer.erik.videogamewishlist.models.Genre;
import com.scheffer.erik.videogamewishlist.models.Platform;
import com.scheffer.erik.videogamewishlist.models.Theme;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import callback.OnSuccessCallback;
import wrapper.IGDBWrapper;
import wrapper.Parameters;
import wrapper.Version;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String PREF_PLATFORMS_SYNC = "platforms_sync";
    public static final String PREF_GENRES_SYNC = "genres_sync";
    public static final String PREF_THEMES_SYNC = "themes_sync";

    private static final String TAG = "SyncAdapter";

    private ContentResolver contentResolver;
    private Context context;

    SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
        this.context = context;
    }

    public SyncAdapter(Context context,
                       boolean autoInitialize,
                       boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver = context.getContentResolver();
        this.context = context;
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {
        Parameters params = new Parameters()
                .addFields("*")
                .addLimit("50");

        final IGDBWrapper wrapper = new IGDBWrapper(BuildConfig.API_KEY,
                                                    Version.STANDARD,
                                                    false);

        wrapper.genres(params, new OnSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                Type listType = new TypeToken<List<Genre>>() {}.getType();
                List<Genre> genres = new Gson().fromJson(result.toString(), listType);

                for (Genre genre : genres) {
                    contentResolver.insert(WishlistContract.GenreEntry.CONTENT_URI,
                                           GenreConverter.toContentValues(genre));
                }

                PreferenceManager.getDefaultSharedPreferences(context).edit()
                                 .putBoolean(PREF_GENRES_SYNC, true).apply();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        wrapper.themes(params, new OnSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                Type listType = new TypeToken<List<Theme>>() {}.getType();
                List<Theme> themes = new Gson().fromJson(result.toString(), listType);

                for (Theme theme : themes) {
                    contentResolver.insert(WishlistContract.ThemeEntry.CONTENT_URI,
                                           ThemeConverter.toContentValues(theme));
                }

                PreferenceManager.getDefaultSharedPreferences(context).edit()
                                 .putBoolean(PREF_THEMES_SYNC, true).apply();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        /*
        The IGDB contains more than 150 platforms. Many are not interesting for this app
        target users. The following list is the ids of the desired platforms manually selected
        from the full list
        */
        String popularPlatformsIds =
                "11," +     // Xbox
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
                        "49";       //Xbox One

        //"38," +     //Xbox Live Arcade
        //"45," +     //PlayStation Network
        params.addIds(popularPlatformsIds);
        wrapper.platforms(params, new OnSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                Type listType = new TypeToken<List<Platform>>() {}.getType();
                List<Platform> platforms = new Gson().fromJson(result.toString(), listType);

                for (Platform platform : platforms) {
                    contentResolver.insert(WishlistContract.PlatformEntry.CONTENT_URI,
                                           PlatformConverter.toContentValues(platform));
                }

                PreferenceManager.getDefaultSharedPreferences(context).edit()
                                 .putBoolean(PREF_PLATFORMS_SYNC, true).apply();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
