package com.scheffer.erik.videogamewishlist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.crashlytics.android.Crashlytics
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.syncadapter.SyncAdapter.Companion.PREF_GENRES_SYNC
import com.scheffer.erik.videogamewishlist.syncadapter.SyncAdapter.Companion.PREF_PLATFORMS_SYNC
import com.scheffer.erik.videogamewishlist.syncadapter.SyncAdapter.Companion.PREF_THEMES_SYNC
import com.scheffer.erik.videogamewishlist.syncadapter.SyncUtils
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_welcome.*
import org.jetbrains.anko.startActivity

class WelcomeActivity : AppCompatActivity() {
    lateinit var sharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    private val isCacheFullySynced: Boolean
        @Synchronized get() = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                .run {
                    getBoolean(PREF_GENRES_SYNC, false) &&
                            getBoolean(PREF_THEMES_SYNC, false) &&
                            getBoolean(PREF_PLATFORMS_SYNC, false)
                }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_welcome)
        fullscreen_content.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        SyncUtils.CreateSyncAccount(this)

        if (isCacheFullySynced) {
            startActivity(Intent(this@WelcomeActivity, WishlistActivity::class.java))
        } else {
            sharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
                if (isCacheFullySynced) {
                    PreferenceManager.getDefaultSharedPreferences(applicationContext)
                            .unregisterOnSharedPreferenceChangeListener(
                                    sharedPreferenceChangeListener)
                    this@WelcomeActivity.startActivity<WishlistActivity>()
                }
            }
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
                    .registerOnSharedPreferenceChangeListener(
                            sharedPreferenceChangeListener)
        }
    }
}
