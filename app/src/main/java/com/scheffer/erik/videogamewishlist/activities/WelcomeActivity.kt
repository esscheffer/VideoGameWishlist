package com.scheffer.erik.videogamewishlist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.ads.consent.*
import com.scheffer.erik.videogamewishlist.BuildConfig
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.syncadapter.SyncAdapter.Companion.PREF_GENRES_SYNC
import com.scheffer.erik.videogamewishlist.syncadapter.SyncAdapter.Companion.PREF_PLATFORMS_SYNC
import com.scheffer.erik.videogamewishlist.syncadapter.SyncAdapter.Companion.PREF_THEMES_SYNC
import com.scheffer.erik.videogamewishlist.syncadapter.SyncUtils
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_welcome.*
import org.jetbrains.anko.startActivity
import java.net.URL

class WelcomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    private lateinit var consentForm: ConsentForm

    private val isCacheFullySynced: Boolean
        @Synchronized get() = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                .run {
                    getBoolean(PREF_GENRES_SYNC, false) &&
                            getBoolean(PREF_THEMES_SYNC, false) &&
                            getBoolean(PREF_PLATFORMS_SYNC, false)
                }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Code to disable crashlytics in debug builds
        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()
        Fabric.with(this, crashlyticsKit)

        setContentView(R.layout.activity_welcome)
        fullscreen_content.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LOW_PROFILE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        SyncUtils.createSyncAccount(this)

        checkConsentStatus()
    }

    private fun handleSync() {
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

    private fun checkConsentStatus() {
        val consentInformation = ConsentInformation.getInstance(this)
        val publisherIds = arrayOf("pub-8540885734073963")
        consentInformation.requestConsentInfoUpdate(publisherIds, object : ConsentInfoUpdateListener {
            override fun onConsentInfoUpdated(consentStatus: ConsentStatus) {
                when(consentStatus) {
                    ConsentStatus.PERSONALIZED -> handleSync()
                    ConsentStatus.UNKNOWN, ConsentStatus.NON_PERSONALIZED -> {
                        if (ConsentInformation.getInstance(this@WelcomeActivity)
                                        .isRequestLocationInEeaOrUnknown) {
                            requestConsent()
                        } else {
                            handleSync()
                        }
                    }
                }
            }

            override fun onFailedToUpdateConsentInfo(errorDescription: String) {
                // User's consent status failed to update.
            }
        })
    }

    private fun requestConsent() {
        val privacyUrl = URL("https://esscheffer.github.io/VideoGameWishlist/")

        consentForm = ConsentForm.Builder(this, privacyUrl)
                .withListener(object : ConsentFormListener() {
                    override fun onConsentFormLoaded() {
                        consentForm.show()
                    }

                    override fun onConsentFormOpened() {
                    }

                    override fun onConsentFormClosed(
                            consentStatus: ConsentStatus?, userPrefersAdFree: Boolean?) {
                        when(consentStatus) {
                            ConsentStatus.PERSONALIZED -> handleSync()
                            else -> finish()
                        }
                    }

                    override fun onConsentFormError(errorDescription: String?) {
                    }
                })
                .withPersonalizedAdsOption()
                .build()

        consentForm.load()
    }
}
