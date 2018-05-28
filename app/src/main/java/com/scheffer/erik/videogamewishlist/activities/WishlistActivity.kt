package com.scheffer.erik.videogamewishlist.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.converters.fromGameDBModelToGame
import com.scheffer.erik.videogamewishlist.database.getAllGames
import com.scheffer.erik.videogamewishlist.models.Game
import com.scheffer.erik.videogamewishlist.recyclerviewadapters.GameRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_wishlist.*
import org.jetbrains.anko.startActivity
import java.util.*

class WishlistActivity : AppCompatActivity() {

    private var gameRecyclerViewAdapter: GameRecyclerViewAdapter? = null
    private lateinit var databaseChangeReceiver: BroadcastReceiver

    private val games = ArrayList<Game>()

    private var reverseOrder: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)
        setSupportActionBar(toolbar)

        search_fab.setOnClickListener {
            startActivity<SearchFormActivity>()
        }

        wishlist_info_text.visibility = View.VISIBLE
        game_list.visibility = View.GONE

        wishlist_info_text.setText(R.string.loading_game_list)

        databaseChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                reloadWishlist()
            }
        }
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(databaseChangeReceiver,
                        IntentFilter(DATABASE_UPDATE_ACTION))

        reloadWishlist()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sort_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_name -> {
                if (reverseOrder) games.sortByDescending { it.name }
                else games.sortBy { it.name }

                reverseOrder = !reverseOrder
                gameRecyclerViewAdapter?.notifyDataSetChanged()
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

    private fun reloadWishlist() {
        games.clear()
        games.addAll(getAllGames().map { fromGameDBModelToGame(it) })

        if (games.isEmpty()) {
            wishlist_info_text.setText(R.string.no_games_found)
            wishlist_info_text.visibility = View.VISIBLE
            game_list.visibility = View.GONE
        } else {
            game_list.visibility = View.VISIBLE
            wishlist_info_text.visibility = View.GONE

            if (gameRecyclerViewAdapter != null) {
                gameRecyclerViewAdapter!!.notifyDataSetChanged()
            } else {
                game_list.layoutManager = LinearLayoutManager(this)
                gameRecyclerViewAdapter = GameRecyclerViewAdapter(games)
                game_list.adapter = gameRecyclerViewAdapter
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(databaseChangeReceiver)
        super.onDestroy()
    }

    companion object {
        const val DATABASE_UPDATE_ACTION = "DATABASE_CHANGE"
    }
}
