package com.scheffer.erik.videogamewishlist.activities

import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.converters.GenreConverter
import com.scheffer.erik.videogamewishlist.converters.PlatformConverter
import com.scheffer.erik.videogamewishlist.converters.ThemeConverter
import com.scheffer.erik.videogamewishlist.database.WishlistContract
import com.scheffer.erik.videogamewishlist.models.Genre
import com.scheffer.erik.videogamewishlist.models.Platform
import com.scheffer.erik.videogamewishlist.models.Theme
import kotlinx.android.synthetic.main.activity_search_form.*
import org.jetbrains.anko.startActivity
import java.util.*

class SearchFormActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private var platforms: MutableList<Platform> = ArrayList()
    private var genres: MutableList<Genre> = ArrayList()
    private var themes: MutableList<Theme> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_form)

        platforms_spinner.setTitle(resources.getString(R.string.select_platform))
        genres_spinner.setTitle(resources.getString(R.string.select_genre))
        themes_spinner.setTitle(resources.getString(R.string.select_theme))

        loaderManager.initLoader(PLATFORMS_LOADER, null, this)
        loaderManager.initLoader(GENRES_LOADER, null, this)
        loaderManager.initLoader(THEMES_LOADER, null, this)

        search_button.setOnClickListener {
            val gameTitle = title_inputText.text.toString()

            var selectedPlatform: Platform? = null
            if (platforms_spinner.selectedItemPosition > 0) {
                selectedPlatform = platforms[platforms_spinner.selectedItemPosition - 1]
            }

            var selectedGenre: Genre? = null
            if (genres_spinner.selectedItemPosition > 0) {
                selectedGenre = genres[genres_spinner.selectedItemPosition - 1]
            }

            var selectedTheme: Theme? = null
            if (themes_spinner.selectedItemPosition > 0) {
                selectedTheme = themes[themes_spinner.selectedItemPosition - 1]
            }

            val minimumRating = rating_rangeBar.leftIndex
            val maximumRating = rating_rangeBar.rightIndex

            this@SearchFormActivity.startActivity<GameSearchListActivity>(
                    GameSearchListActivity.GAME_TITLE_EXTRA to gameTitle,
                    GameSearchListActivity.PLATFORM_EXTRA to selectedPlatform,
                    GameSearchListActivity.GENRE_EXTRA to selectedGenre,
                    GameSearchListActivity.THEME_EXTRA to selectedTheme,
                    GameSearchListActivity.MINIMUM_RATING_EXTRA to minimumRating,
                    GameSearchListActivity.MAXIMUM_RATING_EXTRA to maximumRating)
        }
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<Cursor> {
        val searchUri: Uri
        val orderColumn: String
        when (id) {
            PLATFORMS_LOADER -> {
                searchUri = Uri.withAppendedPath(WishlistContract.BASE_CONTENT_URI,
                                                 WishlistContract.PATH_PLATFORMS)
                orderColumn = WishlistContract.PlatformEntry.COLUMN_NAME
            }
            GENRES_LOADER -> {
                searchUri = Uri.withAppendedPath(WishlistContract.BASE_CONTENT_URI,
                                                 WishlistContract.PATH_GENRES)
                orderColumn = WishlistContract.GenreEntry.COLUMN_NAME
            }
            THEMES_LOADER -> {
                searchUri = Uri.withAppendedPath(WishlistContract.BASE_CONTENT_URI,
                                                 WishlistContract.PATH_THEMES)
                orderColumn = WishlistContract.ThemeEntry.COLUMN_NAME
            }
            else -> throw UnsupportedOperationException("Unkow loader id of: $id")
        }
        return CursorLoader(this, searchUri, null, null, null, orderColumn)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        when (loader.id) {
            PLATFORMS_LOADER -> {
                platforms = ArrayList()
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    platforms.add(PlatformConverter.fromCursor(cursor))
                    cursor.moveToNext()
                }
                val platformsNames = mutableListOf(resources.getString(R.string.any_platform))
                platformsNames.addAll(platforms.map { it.name })
                val platformsAdapter = ArrayAdapter(this,
                                                    android.R.layout.simple_spinner_item,
                                                    platformsNames)
                platformsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                platforms_spinner.adapter = platformsAdapter
            }
            GENRES_LOADER -> {
                genres = ArrayList()
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    genres.add(GenreConverter.fromCursor(cursor))
                    cursor.moveToNext()
                }
                val genresNames = ArrayList<String>()
                genresNames.add(resources.getString(R.string.any_genre))
                for ((_, name) in genres) {
                    genresNames.add(name)
                }
                val genresAdapter = ArrayAdapter(this,
                                                 android.R.layout.simple_spinner_item,
                                                 genresNames)
                genresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                genres_spinner.adapter = genresAdapter
            }
            THEMES_LOADER -> {
                themes = ArrayList()
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    themes.add(ThemeConverter.fromCursor(cursor))
                    cursor.moveToNext()
                }
                val themesNames = mutableListOf(resources.getString(R.string.any_theme))
                themesNames.addAll(themes.map { it.name })
                val themesAdapter = ArrayAdapter(this,
                                                 android.R.layout.simple_spinner_item,
                                                 themesNames)
                themesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                themes_spinner.adapter = themesAdapter
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}

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
        private const val PLATFORMS_LOADER = 101
        private const val GENRES_LOADER = 102
        private const val THEMES_LOADER = 103
    }
}
