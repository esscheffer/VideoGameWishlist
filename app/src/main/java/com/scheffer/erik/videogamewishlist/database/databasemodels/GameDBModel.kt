package com.scheffer.erik.videogamewishlist.database.databasemodels

import com.raizlabs.android.dbflow.annotation.*
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.kotlinextensions.*
import com.raizlabs.android.dbflow.structure.BaseModel
import com.scheffer.erik.videogamewishlist.database.AppDatabase
import com.scheffer.erik.videogamewishlist.models.*


@Table(database = AppDatabase::class, allFields = true)
@MultipleManyToMany(value = [
    ManyToMany(referencedTable = Platform::class),
    ManyToMany(referencedTable = Genre::class),
    ManyToMany(referencedTable = Theme::class)
])
data class GameDBModel(@PrimaryKey var id: Long = 0,
                       var name: String = "",
                       var summary: String? = "",
                       var rating: Double = 0.0,
                       @ForeignKey(
                               saveForeignKeyModel = true,
                               deleteForeignKeyModel = true,
                               onDelete = ForeignKeyAction.CASCADE) var cover: Image? = null
                      ) : BaseModel() {
    var platforms by oneToMany {
        (select from Platform::class
                innerJoin GameDBModel_Platform::class
                on (Platform_Table.id eq GameDBModel_Platform_Table.platform_id)
                where (GameDBModel_Platform_Table.gameDBModel_id eq id))
    }

    var genres by oneToMany {
        (select from Genre::class
                innerJoin GameDBModel_Genre::class
                on (Genre_Table.id eq GameDBModel_Genre_Table.genre_id)
                where (GameDBModel_Genre_Table.gameDBModel_id eq id))
    }

    var themes by oneToMany {
        (select from Theme::class
                innerJoin GameDBModel_Theme::class
                on (Theme_Table.id eq GameDBModel_Theme_Table.theme_id)
                where (GameDBModel_Theme_Table.gameDBModel_id eq id))
    }

    @get:OneToMany(methods = [(OneToMany.Method.ALL)])
    var videos by oneToMany {
        select from Video::class where (Video_Table.gameId_id eq id)
    }
}