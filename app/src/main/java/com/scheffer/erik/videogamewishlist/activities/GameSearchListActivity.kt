package com.scheffer.erik.videogamewishlist.activities

import APICalypse
import IGDBWrapper
import RequestException
import Sort
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.scheffer.erik.videogamewishlist.BuildConfig
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.converters.fromProtoGameToGame
import com.scheffer.erik.videogamewishlist.models.Game
import com.scheffer.erik.videogamewishlist.models.Genre
import com.scheffer.erik.videogamewishlist.models.Platform
import com.scheffer.erik.videogamewishlist.models.Theme
import com.scheffer.erik.videogamewishlist.recyclerviewadapters.GameRecyclerViewAdapter
import games
import kotlinx.android.synthetic.main.activity_game_search_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameSearchListActivity : AppCompatActivity() {

    var games: MutableList<Game> = ArrayList()
    var gameRecyclerViewAdapter: GameRecyclerViewAdapter? = null

    private var reverseOrder: Boolean = false
    private val layoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_search_list)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        game_list.visibility = View.GONE
        search_list_info_text.visibility = View.VISIBLE

        search_list_info_text.setText(R.string.loading_game_list)

        savedInstanceState?.let {
            layoutManager.onRestoreInstanceState(it.getParcelable(LIST_STATE_KEY))
            @Suppress("UNCHECKED_CAST")
            games = it.getParcelableArray(GAME_LIST_KEY).toMutableList() as MutableList<Game>
        }

        if (games.isEmpty()) {
            loadGames()
        } else {
            initializeRecyclerView()
        }
    }

    private fun loadGames() {
        IGDBWrapper.userkey = BuildConfig.API_KEY
        var params = APICalypse()
                .fields("*, cover.*, videos.*")
                .limit(50)
                .sort("rating", Sort.DESCENDING)

        val whereParams = mutableListOf<String>()

        intent.getParcelableExtra<Platform>(PLATFORM_EXTRA)?.let { platform ->
            whereParams.add("platforms = ${platform.id}")
        }

        intent.getParcelableExtra<Genre>(GENRE_EXTRA)?.let { genre ->
            whereParams.add("genres = ${genre.id}")
        }

        intent.getParcelableExtra<Theme>(THEME_EXTRA)?.let { theme ->
            whereParams.add("themes = ${theme.id}")
        }

        val minimumRating = intent.getIntExtra(MINIMUM_RATING_EXTRA, -1)
        if (minimumRating > 0) {
            whereParams.add("rating >= $minimumRating")
        }

        val maximumRating = intent.getIntExtra(MAXIMUM_RATING_EXTRA, 101)
        if (maximumRating < 100) {
            whereParams.add("rating <= $maximumRating")
        }

        val gameTitle = intent.getStringExtra(GAME_TITLE_EXTRA) ?: ""
        if (gameTitle.isNotEmpty()) {
            whereParams.add("name ~ *\"$gameTitle\"*")
        }

        if (whereParams.isNotEmpty()) {
            params = params.where(whereParams.joinToString(" & ") + " & rating != null")
        } else {
            params = params.where("rating != null")
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                games = IGDBWrapper.games(params).map(::fromProtoGameToGame).toMutableList()
                runOnUiThread {
                    if (games.isEmpty()) {
                        search_list_info_text.setText(R.string.no_games_found)
                    } else {
                        initializeRecyclerView()
                    }
                }
            } catch (error: RequestException) {
                runOnUiThread { search_list_info_text.setText(R.string.error_games_from_server) }
                error.printStackTrace()
            }
        }
    }

    private fun initializeRecyclerView() {
        game_list.visibility = View.VISIBLE
        search_list_info_text.visibility = View.GONE

        game_list.layoutManager = layoutManager
        gameRecyclerViewAdapter = GameRecyclerViewAdapter(games)
        game_list.adapter = gameRecyclerViewAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sort_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.sort_name -> {
                if (reverseOrder) games.sortByDescending { it.name }
                else games.sortBy { it.name }

                gameRecyclerViewAdapter?.notifyDataSetChanged()
                reverseOrder = !reverseOrder
                return true
            }
            R.id.sort_rating -> {
                if (reverseOrder) games.sortByDescending { it.rating }
                else games.sortBy { it.rating }

                gameRecyclerViewAdapter?.notifyDataSetChanged()
                reverseOrder = !reverseOrder
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putParcelable(LIST_STATE_KEY, layoutManager.onSaveInstanceState())
        state.putParcelableArray(GAME_LIST_KEY, games.toTypedArray())
    }

    companion object {
        const val GAME_TITLE_EXTRA = "game-title"
        const val PLATFORM_EXTRA = "platform"
        const val GENRE_EXTRA = "genre"
        const val THEME_EXTRA = "theme"
        const val MINIMUM_RATING_EXTRA = "minimum-rating"
        const val MAXIMUM_RATING_EXTRA = "maximum-rating"

        private const val LIST_STATE_KEY = "list-state"
        private const val GAME_LIST_KEY = "game-list"
    }
}
