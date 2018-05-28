package com.scheffer.erik.videogamewishlist.utils

import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.fastSave
import com.raizlabs.android.dbflow.structure.BaseModel
import com.scheffer.erik.videogamewishlist.database.AppDatabase

inline fun <reified T : BaseModel> List<T>.fastSaveAll() {
    FlowManager.getDatabase(AppDatabase::class.java).executeTransaction(this.fastSave().build())
}
