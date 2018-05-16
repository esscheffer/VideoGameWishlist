package com.scheffer.erik.videogamewishlist.recyclerviewadapters

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.models.Video
import kotlinx.android.synthetic.main.video_list_item.view.*
import org.jetbrains.anko.toast

class VideoAdapter(private val videos: List<Video>) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context)
                               .inflate(R.layout.video_list_item, parent, false)
                      )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]
        holder.itemView.video_text.text = video.name
        holder.video = video
    }

    override fun getItemCount() = videos.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var video: Video

        init {
            itemView.setOnClickListener {
                val youtubeActivity = Intent(Intent.ACTION_VIEW, buildYoutubeUri(video.videoId))
                if (youtubeActivity.resolveActivity(itemView.context
                                                            .packageManager) != null) {
                    itemView.context.startActivity(youtubeActivity)
                } else {
                    itemView.context.toast(R.string.nothing_to_show_video)
                }
            }
        }

        private fun buildYoutubeUri(key: String): Uri {
            return Uri.parse("https://www.youtube.com/watch")
                    .buildUpon()
                    .appendQueryParameter("v", key)
                    .build()
        }
    }
}