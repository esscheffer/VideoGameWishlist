package com.scheffer.erik.videogamewishlist.recyclerviewadapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.activities.GameDetailActivity
import com.scheffer.erik.videogamewishlist.activities.GameDetailActivity.Companion.GAME_EXTRA
import com.scheffer.erik.videogamewishlist.converters.gameToIGDBGame
import com.scheffer.erik.videogamewishlist.models.Game
import com.scheffer.erik.videogamewishlist.utils.IGDBImageUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.game_list_item.view.*
import org.jetbrains.anko.startActivity

class GameRecyclerViewAdapter(private val games: List<Game>)
    : RecyclerView.Adapter<GameRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context)
                               .inflate(R.layout.game_list_item, parent, false)
                      )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.run {
            game = games[position]
            view.game_title.text = games[position].name
            view.game_rating.text = String.format("%.2f", games[position].rating)
            val cover = game.cover
            if (cover != null) {
                Picasso.get()
                        .load(IGDBImageUtils.getImageUrl(cover, IGDBImageUtils.THUMB))
                        .placeholder(R.drawable.ic_image_off_black_24dp)
                        .into(holder.view.cover_image)
            } else {
                holder.view.cover_image.setImageResource(R.drawable.ic_image_off_black_24dp)
            }

            holder.view.setOnClickListener { view ->
                view.context.startActivity<GameDetailActivity>(GAME_EXTRA to gameToIGDBGame(holder.game))
            }
        }
    }

    override fun getItemCount() = games.size

    inner class ViewHolder internal constructor(val view: View) : RecyclerView.ViewHolder(view) {
        lateinit var game: Game
    }
}
