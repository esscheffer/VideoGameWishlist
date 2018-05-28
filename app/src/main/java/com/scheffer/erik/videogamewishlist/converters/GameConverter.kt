package com.scheffer.erik.videogamewishlist.converters

import com.scheffer.erik.videogamewishlist.database.databasemodels.GameDBModel
import com.scheffer.erik.videogamewishlist.database.getGenresByIdList
import com.scheffer.erik.videogamewishlist.database.getPlatformsByIdList
import com.scheffer.erik.videogamewishlist.database.getThemesByIdList
import com.scheffer.erik.videogamewishlist.models.Game
import com.scheffer.erik.videogamewishlist.models.IGDBGame

fun fromGameToGameDBModel(game: Game) =
        GameDBModel().apply {
            id = game.id
            name = game.name
            summary = game.summary
            rating = game.rating
            cover = game.cover
            platforms = game.platforms
            genres = game.genres
            themes = game.themes
            videos = game.videos
        }

fun fromGameDBModelToGame(gameDBModel: GameDBModel) =
        Game(gameDBModel.id,
                gameDBModel.name,
                gameDBModel.summary,
                gameDBModel.rating,
                gameDBModel.cover,
                gameDBModel.platforms,
                gameDBModel.genres,
                gameDBModel.themes,
                gameDBModel.videos)

fun fromIGDBGameToGame(igdbGame: IGDBGame) =
        Game().apply {
            id = igdbGame.id
            name = igdbGame.name
            summary = igdbGame.summary
            rating = igdbGame.rating
            cover = igdbGame.cover
            videos = igdbGame.videos

            platforms = igdbGame.platforms?.let { platformIds -> getPlatformsByIdList(platformIds) }
            genres = igdbGame.genres?.let { genreIds -> getGenresByIdList(genreIds) }
            themes = igdbGame.themes?.let { themeIds -> getThemesByIdList(themeIds) }
        }
