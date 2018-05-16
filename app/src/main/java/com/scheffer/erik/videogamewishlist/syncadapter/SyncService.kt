package com.scheffer.erik.videogamewishlist.syncadapter

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SyncService : Service() {

    private var syncAdapter: SyncAdapter? = null

    override fun onCreate() {
        synchronized(syncAdapterLock) {
            if (syncAdapter == null) {
                syncAdapter = SyncAdapter(applicationContext, true)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return syncAdapter!!.syncAdapterBinder
    }

    companion object {
        private val syncAdapterLock = Any()
    }
}
