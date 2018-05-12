package com.scheffer.erik.videogamewishlist.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.syncadapter.SyncUtils;

import static com.scheffer.erik.videogamewishlist.syncadapter.SyncAdapter.PREF_GENRES_SYNC;
import static com.scheffer.erik.videogamewishlist.syncadapter.SyncAdapter.PREF_PLATFORMS_SYNC;
import static com.scheffer.erik.videogamewishlist.syncadapter.SyncAdapter.PREF_THEMES_SYNC;

public class WelcomeActivity extends AppCompatActivity {
    SharedPreferences.OnSharedPreferenceChangeListener spChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViewById(R.id.fullscreen_content)
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                                               | View.SYSTEM_UI_FLAG_FULLSCREEN
                                               | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                               | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                               | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                               | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        SyncUtils.CreateSyncAccount(this);

        if (isCacheFullySynced()) {
            startActivity(new Intent(WelcomeActivity.this, WishlistActivity.class));
        } else {

            spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    if (isCacheFullySynced()) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                         .unregisterOnSharedPreferenceChangeListener(spChanged);
                        WelcomeActivity.this
                                .startActivity(new Intent(WelcomeActivity.this,
                                                          WishlistActivity.class));
                    }
                }
            };
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                             .registerOnSharedPreferenceChangeListener(spChanged);
        }
    }

    private boolean isCacheFullySynced() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        return sharedPreferences.getBoolean(PREF_GENRES_SYNC, false) &&
                sharedPreferences.getBoolean(PREF_THEMES_SYNC, false) &&
                sharedPreferences.getBoolean(PREF_PLATFORMS_SYNC, false);
    }
}
