package com.scheffer.erik.videogamewishlist.converters

import com.scheffer.erik.videogamewishlist.database.databasemodels.GameDBModel
import com.scheffer.erik.videogamewishlist.database.getGenresByIdList
import com.scheffer.erik.videogamewishlist.database.getPlatformsByIdList
import com.scheffer.erik.videogamewishlist.database.getThemesByIdList
import com.scheffer.erik.videogamewishlist.models.Game
import com.scheffer.erik.videogamewishlist.models.Image
import com.scheffer.erik.videogamewishlist.models.Video

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

fun fromProtoGameToGame(game: proto.Game) =
        Game().apply {
            id = game.id
            name = game.name
            summary = game.summary
            rating = game.rating
            cover = Image(game.cover.url, game.cover.imageId)
            videos = game.videosList.map { Video(it.name, it.videoId, game.id) }

            platforms = game.platformsList?.let { platforms ->
                getPlatformsByIdList(platforms.map(proto.Platform::getId))
            }
            genres = game.genresList?.let { genres ->
                getGenresByIdList(genres.map(proto.Genre::getId))
            }
            themes = game.themesList?.let { themes ->
                getThemesByIdList(themes.map(proto.Theme::getId))
            }
        }