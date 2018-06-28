package com.scheffer.erik.videogamewishlist.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.converters.fromGameToGameDBModel
import com.scheffer.erik.videogamewishlist.database.databasemodels.GameDBModel
import com.scheffer.erik.videogamewishlist.database.getGameById
import com.scheffer.erik.videogamewishlist.database.repositories.deleteGameDBModel
import com.scheffer.erik.videogamewishlist.database.repositories.saveGameDBModel
import com.scheffer.erik.videogamewishlist.models.Game
import com.scheffer.erik.videogamewishlist.recyclerviewadapters.VideoAdapter
import com.scheffer.erik.videogamewishlist.utils.IGDBImageUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game_detail.*
import kotlinx.android.synthetic.main.content_game_detail.*
import org.jetbrains.anko.toast


class GameDetailActivity : AppCompatActivity() {

    private var isSaved = false
    private lateinit var game: Game
    private val gameDBModel: GameDBModel by lazy {
        fromGameToGameDBModel(game)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        MobileAds.initialize(this, "ca-app-pub-8540885734073963~9962865212")

        adView.loadAd(AdRequest.Builder().build())

        game = intent.getParcelableExtra(GAME_EXTRA) ?: Game()

        toolbar.title = game.name
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        game.cover?.let { cover ->
            Picasso.get()
                    .load(IGDBImageUtils.getImageUrl(cover, IGDBImageUtils.SIZE_720P))
                    .placeholder(R.drawable.ic_image_off_black_24dp)
                    .into(toolbar_cover)
        }

        summary_text.text = game.summary
        rating_text.text = String.format("%.2f", game.rating)
        platforms_text.text = game.platforms?.joinToString { it.name }
        genres_text.text = game.genres?.joinToString { it.name }
        themes_text.text = game.themes?.joinToString { it.name }

        game.videos?.let { videos ->
            videos_recyclerView.run {
                val linearLayoutManager = LinearLayoutManager(this@GameDetailActivity)
                setHasFixedSize(true)
                layoutManager = linearLayoutManager
                addItemDecoration(DividerItemDecoration(this@GameDetailActivity,
                        linearLayoutManager.orientation))
                adapter = VideoAdapter(videos)
            }
        }

        setSaved(getGameById(game.id) != null)

        fab.setOnClickListener {
            if (isSaved) {
                deleteGameDBModel(gameDBModel,
                        { setSaved(false) },
                        { _, _ -> toast(R.string.error_deleting) })
            } else {
                saveGameDBModel(gameDBModel,
                        { setSaved(true) },
                        { _, _ -> toast(R.string.error_saving) })
            }
            LocalBroadcastManager.getInstance(this@GameDetailActivity)
                    .sendBroadcast(Intent(WishlistActivity.DATABASE_UPDATE_ACTION))
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

    private fun setSaved(isSaved: Boolean) {
        this.isSaved = isSaved
        fab.setImageDrawable(
                ContextCompat.getDrawable(this,
                        if (isSaved) R.drawable.ic_favorite_black_24dp
                        else R.drawable.ic_favorite_border_black_24dp
                                         ))
    }

    companion object {
        const val GAME_EXTRA = "game-extra"
    }
}
