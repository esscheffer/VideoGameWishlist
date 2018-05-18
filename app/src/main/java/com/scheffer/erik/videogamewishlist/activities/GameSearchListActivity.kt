package com.scheffer.erik.videogamewishlist.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import callback.OnSuccessCallback
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.scheffer.erik.videogamewishlist.BuildConfig
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.database.WishlistContract
import com.scheffer.erik.videogamewishlist.models.*
import com.scheffer.erik.videogamewishlist.recyclerviewadapters.GameRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_game_search_list.*
import org.json.JSONArray
import wrapper.Endpoints
import wrapper.IGDBWrapper
import wrapper.Parameters
import wrapper.Version
import java.util.*

class GameSearchListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_search_list)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        game_list.visibility = View.GONE
        search_list_info_text.visibility = View.VISIBLE

        search_list_info_text.setText(R.string.loading_game_list)

        val wrapper = IGDBWrapper(BuildConfig.API_KEY, Version.STANDARD, false)

        val params = Parameters()
                .addFields("*")
                .addOrder("rating:desc")
                .addLimit("50")

        val platform = intent.getParcelableExtra<Platform>(PLATFORM_EXTRA)
        if (platform != null) {
            params.addFilter("[platforms][eq]=${platform.id}")
        }

        val genre = intent.getParcelableExtra<Genre>(GENRE_EXTRA)
        if (genre != null) {
            params.addFilter("[genres][eq]=${genre.id}")
        }

        val theme = intent.getParcelableExtra<Theme>(THEME_EXTRA)
        if (theme != null) {
            params.addFilter("[theme][eq]=${theme.id}")
        }

        val minimumRating = intent.getIntExtra(MINIMUM_RATING_EXTRA, -1)
        if (minimumRating > 0) {
            params.addFilter("[rating][gte]=$minimumRating")
        }

        val maximumRating = intent.getIntExtra(MAXIMUM_RATING_EXTRA, 101)
        if (maximumRating < 100) {
            params.addFilter("[rating][lte]=$maximumRating")
        }

        val gameTitle = intent.getStringExtra(GAME_TITLE_EXTRA) ?: ""
        if (!gameTitle.isEmpty()) {
            params.addSearch(gameTitle)
            wrapper.search(Endpoints.GAMES, params, MySuccessCallback())
        } else {
            wrapper.games(params, MySuccessCallback())
        }
    }

    private inner class MySuccessCallback : OnSuccessCallback {

        override fun onSuccess(result: JSONArray) {

            val listType = object : TypeToken<ArrayList<IGDBGame>>() {

            }.type
            val igdbGames = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
                    .fromJson<ArrayList<IGDBGame>>(result.toString(), listType)

            val games = ArrayList<Game>()
            for (igdbGame in igdbGames) {
                val game = Game()
                game.id = igdbGame.id
                game.name = igdbGame.name
                game.summary = igdbGame.summary
                game.rating = igdbGame.rating
                game.cover = igdbGame.cover
                game.videos = igdbGame.videos

                val platforms = ArrayList<Platform>()
                if (igdbGame.platforms != null) {
                    for (platformId in igdbGame.platforms as List) {
                        val cursor = contentResolver
                                .query(WishlistContract.PlatformEntry.CONTENT_URI.buildUpon()
                                               .appendPath(platformId.toString())
                                               .build(), null, null, null, null)
                        if (cursor != null) {
                            if (cursor.count > 0) {
                                cursor.moveToFirst()
                                platforms.add(Platform(platformId,
                                                       cursor.getString(cursor.getColumnIndex(
                                                               WishlistContract.PlatformEntry.COLUMN_NAME))))
                            }
                            cursor.close()
                        }
                    }
                }
                game.platforms = platforms

                val genres = ArrayList<Genre>()
                if (igdbGame.genres != null) {
                    for (genreId in igdbGame.genres as List) {
                        val cursor = contentResolver
                                .query(WishlistContract.GenreEntry.CONTENT_URI.buildUpon()
                                               .appendPath(
                                                       genreId.toString())
                                               .build(), null, null, null, null)
                        if (cursor != null) {
                            if (cursor.count > 0) {
                                cursor.moveToFirst()
                                genres.add(Genre(genreId,
                                                 cursor.getString(cursor.getColumnIndex(
                                                         WishlistContract.ThemeEntry.COLUMN_NAME))))
                            }
                            cursor.close()
                        }
                    }
                }
                game.genres = genres

                val themes = ArrayList<Theme>()
                if (igdbGame.themes != null) {
                    for (themeId in igdbGame.themes as List) {
                        val cursor = contentResolver
                                .query(WishlistContract.ThemeEntry.CONTENT_URI.buildUpon()
                                               .appendPath(
                                                       themeId.toString())
                                               .build(), null, null, null, null)
                        if (cursor != null) {
                            if (cursor.count > 0) {
                                cursor.moveToFirst()
                                themes.add(Theme(themeId,
                                                 cursor.getString(cursor.getColumnIndex(
                                                         WishlistContract.ThemeEntry.COLUMN_NAME))))
                            }
                            cursor.close()
                        }
                    }
                }
                game.themes = themes

                games.add(game)
            }

            runOnUiThread {
                if (games.isEmpty()) {
                    search_list_info_text.setText(R.string.no_games_found)
                } else {
                    game_list.visibility = View.VISIBLE
                    search_list_info_text.visibility = View.GONE

                    game_list.layoutManager = LinearLayoutManager(this@GameSearchListActivity)
                    game_list.adapter = GameRecyclerViewAdapter(games)
                }
            }
        }

        override fun onError(error: Exception) {
            runOnUiThread { search_list_info_text.setText(R.string.error_games_from_server) }
            error.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val GAME_TITLE_EXTRA = "game-title"
        const val PLATFORM_EXTRA = "platform"
        const val GENRE_EXTRA = "genre"
        const val THEME_EXTRA = "theme"
        const val MINIMUM_RATING_EXTRA = "minimum-rating"
        const val MAXIMUM_RATING_EXTRA = "maximum-rating"
    }
}
