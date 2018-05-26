package com.scheffer.erik.videogamewishlist.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.models.Genre
import com.scheffer.erik.videogamewishlist.models.Platform
import com.scheffer.erik.videogamewishlist.models.Theme
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_search_form.*
import org.jetbrains.anko.startActivity

class SearchFormActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    private lateinit var platforms: RealmResults<Platform>
    private lateinit var genres: RealmResults<Genre>
    private lateinit var themes: RealmResults<Theme>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_form)

        platforms_spinner.setTitle(resources.getString(R.string.select_platform))
        genres_spinner.setTitle(resources.getString(R.string.select_genre))
        themes_spinner.setTitle(resources.getString(R.string.select_theme))

        realm = Realm.getDefaultInstance()

        loadPlatforms()
        loadGenres()
        loadThemes()

        search_button.setOnClickListener {
            val gameTitle = title_inputText.text.toString()

            var selectedPlatformId = -1L
            if (platforms_spinner.selectedItemPosition > 0) {
                selectedPlatformId = platforms[platforms_spinner.selectedItemPosition - 1]?.id ?: -1
            }

            var selectedGenreId = -1L
            if (genres_spinner.selectedItemPosition > 0) {
                selectedGenreId = genres[genres_spinner.selectedItemPosition - 1]?.id ?: -1
            }

            var selectedThemeId = -1L
            if (themes_spinner.selectedItemPosition > 0) {
                selectedThemeId = themes[themes_spinner.selectedItemPosition - 1]?.id ?: -1
            }

            val minimumRating = rating_rangeBar.leftIndex
            val maximumRating = rating_rangeBar.rightIndex

            this@SearchFormActivity.startActivity<GameSearchListActivity>(
                    GameSearchListActivity.GAME_TITLE_EXTRA to gameTitle,
                    GameSearchListActivity.PLATFORM_ID_EXTRA to selectedPlatformId,
                    GameSearchListActivity.GENRE_ID_EXTRA to selectedGenreId,
                    GameSearchListActivity.THEME_ID_EXTRA to selectedThemeId,
                    GameSearchListActivity.MINIMUM_RATING_EXTRA to minimumRating,
                    GameSearchListActivity.MAXIMUM_RATING_EXTRA to maximumRating)
        }
    }

    private fun loadPlatforms() {
        platforms = realm.where<Platform>().findAll()
        val platformsNames = mutableListOf(resources.getString(R.string.any_platform))
        platformsNames.addAll(platforms.map { it.name })

        platforms_spinner.adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                platformsNames)
                .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    private fun loadGenres() {
        genres = realm.where<Genre>().findAll()
        val genresNames = mutableListOf(resources.getString(R.string.any_genre))
        genresNames.addAll(genres.map { it.name })
        genres_spinner.adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                genresNames)
                .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    private fun loadThemes() {
        themes = realm.where<Theme>().findAll()
        val themesNames = mutableListOf(resources.getString(R.string.any_theme))
        themesNames.addAll(themes.map { it.name })
        themes_spinner.adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                themesNames)
                .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
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

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
