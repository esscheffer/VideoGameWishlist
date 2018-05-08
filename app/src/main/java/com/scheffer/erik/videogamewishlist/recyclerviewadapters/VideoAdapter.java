package com.scheffer.erik.videogamewishlist.recyclerviewadapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.models.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<Video> videos;

    public VideoAdapter(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.video_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.videoText.setText(video.getName());
        holder.video = video;
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView videoText;
        Video video;

        ViewHolder(final View itemView) {
            super(itemView);
            this.videoText = itemView.findViewById(R.id.video_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent youtubeActivity =
                            new Intent(Intent.ACTION_VIEW, buildYoutubeUri(video.getVideoId()));
                    if (youtubeActivity.resolveActivity(itemView.getContext()
                                                                .getPackageManager()) != null) {
                        itemView.getContext().startActivity(youtubeActivity);
                    } else {
                        Toast.makeText(itemView.getContext(),
                                       R.string.nothing_to_show_video,
                                       Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private Uri buildYoutubeUri(String key) {
            return Uri.parse("https://www.youtube.com/watch")
                      .buildUpon()
                      .appendQueryParameter("v", key)
                      .build();
        }
    }
}