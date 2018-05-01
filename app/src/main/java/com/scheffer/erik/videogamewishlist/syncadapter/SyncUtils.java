package com.scheffer.erik.videogamewishlist.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.scheffer.erik.videogamewishlist.database.WishlistContract;

/**
 * Static helper methods for working with the sync framework.
 */
public class SyncUtils {
    private static final long SYNC_FREQUENCY = 60 * 60 * 24;  // 24 hour (in seconds)
    private static final String CONTENT_AUTHORITY = WishlistContract.AUTHORITY;
    private static final String PREF_SETUP_COMPLETE = "setup_complete";
    public static final String ACCOUNT_TYPE = "com.scheffer.erik.videogamewishlist.account";

    public static void CreateSyncAccount(Context context) {
        boolean newAccount = false;
        boolean setupComplete = PreferenceManager.getDefaultSharedPreferences(context)
                                                 .getBoolean(PREF_SETUP_COMPLETE, false);

        Account account = GenericAccountService.GetAccount(ACCOUNT_TYPE);
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager != null &&
                accountManager.addAccountExplicitly(account, null, null)) {
            ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
            ContentResolver.addPeriodicSync(account,
                                            CONTENT_AUTHORITY,
                                            new Bundle(),
                                            SYNC_FREQUENCY);
            newAccount = true;
        }

        if (newAccount || !setupComplete) {
            TriggerRefresh();
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                             .putBoolean(PREF_SETUP_COMPLETE, true).apply();
        }
    }

    private static void TriggerRefresh() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(
                GenericAccountService.GetAccount(ACCOUNT_TYPE),
                WishlistContract.AUTHORITY,
                bundle);
    }
}
