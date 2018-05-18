package com.scheffer.erik.videogamewishlist.activities

import android.app.LoaderManager
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.converters.GameConverter
import com.scheffer.erik.videogamewishlist.database.WishlistContract
import com.scheffer.erik.videogamewishlist.models.Game
import com.scheffer.erik.videogamewishlist.recyclerviewadapters.GameRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_wishlist.*
import org.jetbrains.anko.startActivity
import java.util.*

class WishlistActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    var gameRecyclerViewAdapter: GameRecyclerViewAdapter? = null
    lateinit var databaseChangeReceiver: BroadcastReceiver

    private val games = ArrayList<Game>()

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
        loaderManager.initLoader(GAMES_LOADER, null, this)

        databaseChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                loaderManager.restartLoader(GAMES_LOADER, null, this@WishlistActivity)
            }
        }
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(databaseChangeReceiver,
                                  IntentFilter(DATABASE_UPDATE_ACTION))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_wishlist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        return CursorLoader(this,
                            Uri.withAppendedPath(WishlistContract.BASE_CONTENT_URI,
                                                 WishlistContract.PATH_GAMES), null, null, null,
                            WishlistContract.GameEntry.COLUMN_RATING + " DESC")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        games.clear()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            games.add(GameConverter.fromCursor(cursor))
            cursor.moveToNext()
        }

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

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(databaseChangeReceiver)
        super.onDestroy()
    }

    companion object {
        private const val GAMES_LOADER = 501
        const val DATABASE_UPDATE_ACTION = "DATABASE_CHANGE"
    }
}
