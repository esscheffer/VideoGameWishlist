package com.scheffer.erik.videogamewishlist.converters

import com.scheffer.erik.videogamewishlist.models.*
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.oneOf
import io.realm.kotlin.where

fun IGDBGameToGame(igdbGame: IGDBGame): Game {
    val game = Game()
    game.id = igdbGame.id
    game.name = igdbGame.name
    game.summary = igdbGame.summary
    game.rating = igdbGame.rating
    game.cover = igdbGame.cover
    game.videos = RealmList()
    igdbGame.videos?.let {
        game.videos?.addAll(it)
    }

    val realm = Realm.getDefaultInstance()

    game.platforms = RealmList()
    igdbGame.platforms?.let {
        val platformsResult = realm.where<Platform>().oneOf("id", it.toTypedArray()).findAll()
        game.platforms?.addAll(platformsResult.subList(0, platformsResult.size))
    }

    game.genres = RealmList()
    igdbGame.genres?.let {
        val genresResult = realm.where<Genre>().oneOf("id", it.toTypedArray()).findAll()
        game.genres?.addAll(genresResult.subList(0, genresResult.size))
    }

    game.themes = RealmList()
    igdbGame.themes?.let {
        val themesResult = realm.where<Theme>().oneOf("id", it.toTypedArray()).findAll()
        game.themes?.addAll(themesResult.subList(0, themesResult.size))
    }

    realm.close()
    return game
}

fun gameToIGDBGame(game: Game): IGDBGame {
    val igdbGame = IGDBGame()
    igdbGame.id = game.id
    igdbGame.name = game.name
    igdbGame.summary = game.summary
    igdbGame.rating = game.rating
    igdbGame.cover = game.cover
    igdbGame.videos = game.videos
    igdbGame.platforms = game.platforms?.map { it.id }
    igdbGame.genres = game.genres?.map { it.id }
    igdbGame.themes = game.themes?.map { it.id }
    return igdbGame
}