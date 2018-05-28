package com.scheffer.erik.videogamewishlist.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ArrayAdapter
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

        platforms_spinner.setTitle(resources.getString(R.string.select_platform))
        genres_spinner.setTitle(resources.getString(R.string.select_genre))
        themes_spinner.setTitle(resources.getString(R.string.select_theme))

        loadPlatforms()
        loadGenres()
        loadThemes()

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

    private fun loadPlatforms() {
        platforms = getAllPlatforms()
        val platformsNames = mutableListOf(resources.getString(R.string.any_platform))
        platformsNames.addAll(platforms.map { it.name })
        platforms_spinner.adapter = generateStringAdapter(platformsNames)
    }

    private fun loadGenres() {
        genres = getAllGenres()
        val genresNames = mutableListOf(resources.getString(R.string.any_genre))
        genresNames.addAll(genres.map { it.name })
        genres_spinner.adapter = generateStringAdapter(genresNames)
    }

    private fun loadThemes() {
        themes = getAllThemes()
        val themesNames = mutableListOf(resources.getString(R.string.any_theme))
        themesNames.addAll(themes.map { it.name })
        themes_spinner.adapter = generateStringAdapter(themesNames)
    }

    private fun generateStringAdapter(stringList: List<String>) =
            ArrayAdapter(this,
                    android.R.layout.simple_spinner_item,
                    stringList)
                    .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

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
