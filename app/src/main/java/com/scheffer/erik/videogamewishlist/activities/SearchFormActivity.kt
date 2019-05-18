package com.scheffer.erik.videogamewishlist.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.database.getAllGenres
import com.scheffer.erik.videogamewishlist.database.getAllPlatforms
import com.scheffer.erik.videogamewishlist.database.getAllThemes
import com.scheffer.erik.videogamewishlist.models.Genre
import com.scheffer.erik.videogamewishlist.models.Platform
import com.scheffer.erik.videogamewishlist.models.Theme
import kotlinx.android.synthetic.main.activity_search_form.*
import org.jetbrains.anko.startActivity

class SearchFormActivity : AppCompatActivity() {

    private var platforms: MutableList<Platform> = ArrayList()
    private var genres: MutableList<Genre> = ArrayList()
    private var themes: MutableList<Theme> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_form)

        loadPlatforms()
        loadGenres()
        loadThemes()

        search_button.setOnClickListener {
            val gameTitle = title_inputText.text.toString()

            var selectedPlatform: Platform? = null
            if (platforms_spinner.selectedIndex > 0) {
                selectedPlatform = platforms[platforms_spinner.selectedIndex - 1]
            }

            var selectedGenre: Genre? = null
            if (genres_spinner.selectedIndex > 0) {
                selectedGenre = genres[genres_spinner.selectedIndex - 1]
            }

            var selectedTheme: Theme? = null
            if (themes_spinner.selectedIndex > 0) {
                selectedTheme = themes[themes_spinner.selectedIndex - 1]
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

    private fun loadPlatforms() {
        platforms = getAllPlatforms()
        val platformsNames = mutableListOf(resources.getString(R.string.any_platform))
        platformsNames.addAll(platforms.map { it.name })
        platforms_spinner.setItems(platformsNames)
    }

    private fun loadGenres() {
        genres = getAllGenres()
        val genresNames = mutableListOf(resources.getString(R.string.any_genre))
        genresNames.addAll(genres.map { it.name })
        genres_spinner.setItems(genresNames)
    }

    private fun loadThemes() {
        themes = getAllThemes()
        val themesNames = mutableListOf(resources.getString(R.string.any_theme))
        themesNames.addAll(themes.map { it.name })
        themes_spinner.setItems(themesNames)
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
}
