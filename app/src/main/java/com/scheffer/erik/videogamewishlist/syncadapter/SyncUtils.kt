package com.scheffer.erik.videogamewishlist.syncadapter

import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager

/**
 * Static helper methods for working with the sync framework.
 */
object SyncUtils {
    private const val SYNC_FREQUENCY = (60 * 60 * 24 * 30).toLong()  // 30 days (in seconds)
    private const val CONTENT_AUTHORITY = "com.scheffer.erik.videogamewishlist"
    private const val PREF_SETUP_COMPLETE = "setup_complete"
    private const val ACCOUNT_TYPE = "com.scheffer.erik.videogamewishlist.account"

    @JvmStatic
    fun createSyncAccount(context: Context) {
        var newAccount = false
        val setupComplete = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_SETUP_COMPLETE, false)

        val account = GenericAccountService.GetAccount(ACCOUNT_TYPE)
        val accountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
        if (accountManager.addAccountExplicitly(account, null, null)) {
            ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1)
            ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true)
            ContentResolver.addPeriodicSync(account,
                                            CONTENT_AUTHORITY,
                                            Bundle(),
                                            SYNC_FREQUENCY)
            newAccount = true
        }

        if (!newAccount && !setupComplete) {
            triggerRefresh()
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(PREF_SETUP_COMPLETE, true).apply()
        }
    }

    private fun triggerRefresh() {
        val bundle = Bundle().apply {
            putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
            putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
        }
        ContentResolver.requestSync(
                GenericAccountService.GetAccount(ACCOUNT_TYPE),
                CONTENT_AUTHORITY,
                bundle)
    }
}
