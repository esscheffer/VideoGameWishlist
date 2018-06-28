package com.scheffer.erik.videogamewishlist.database.repositories

import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction
import com.scheffer.erik.videogamewishlist.database.AppDatabase
import com.scheffer.erik.videogamewishlist.database.databasemodels.*
import com.scheffer.erik.videogamewishlist.utils.fastSaveAll

fun saveGameDBModel(gameDBModel: GameDBModel,
                    onSuccess: () -> Unit = {},
                    onError: (transaction: Transaction, error: Throwable) -> Unit = { _, _ -> }) {
    FlowManager.getDatabase(AppDatabase::class.java).beginTransactionAsync { databaseWrapper ->
        gameDBModel.videos?.forEach { it.gameId = gameDBModel.id }
        gameDBModel.save(databaseWrapper)

        gameDBModel.platforms?.map { platform ->
            GameDBModel_Platform().also { relation ->
                relation.gameDBModel = gameDBModel
                relation.platform = platform
            }
        }?.fastSaveAll()

        gameDBModel.genres?.map { genre ->
            GameDBModel_Genre().also { relation ->
                relation.gameDBModel = gameDBModel
                relation.genre = genre
            }
        }?.fastSaveAll()

        gameDBModel.themes?.map { theme ->
            GameDBModel_Theme().also { relation ->
                relation.gameDBModel = gameDBModel
                relation.theme = theme
            }
        }?.fastSaveAll()
    }
            .success { onSuccess() }
            .error { transaction, error -> onError(transaction, error); }
            .execute()
}

fun deleteGameDBModel(gameDBModel: GameDBModel,
                      onSuccess: () -> Unit = {},
                      onError: (transaction: Transaction, error: Throwable) -> Unit = { _, _ -> }) {
    FlowManager.getDatabase(AppDatabase::class.java).beginTransactionAsync { databaseWrapper ->
        val idTemp = gameDBModel.id
        val videosTemp = gameDBModel.videos
        gameDBModel.videos?.forEach { it.gameId = gameDBModel.id }

        Delete().from(GameDBModel_Platform::class.java)
                .where(GameDBModel_Platform_Table.gameDBModel_id.eq(gameDBModel.id))
                .execute()

        Delete().from(GameDBModel_Genre::class.java)
                .where(GameDBModel_Genre_Table.gameDBModel_id.eq(gameDBModel.id))
                .execute()

        Delete().from(GameDBModel_Theme::class.java)
                .where(GameDBModel_Theme_Table.gameDBModel_id.eq(gameDBModel.id))
                .execute()

        gameDBModel.delete(databaseWrapper)
        gameDBModel.id = idTemp
        gameDBModel.videos = videosTemp
    }
            .success { onSuccess() }
            .error { transaction, error -> onError(transaction, error); }
            .execute()
}