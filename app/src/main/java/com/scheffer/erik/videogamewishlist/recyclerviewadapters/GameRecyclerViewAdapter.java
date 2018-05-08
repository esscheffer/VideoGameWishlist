package com.scheffer.erik.videogamewishlist.recyclerviewadapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.activities.GameDetailActivity;
import com.scheffer.erik.videogamewishlist.models.Game;
import com.scheffer.erik.videogamewishlist.utils.IGDBImageUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scheffer.erik.videogamewishlist.activities.GameDetailActivity.GAME_EXTRA;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.ViewHolder> {

    private final List<Game> games;

    public GameRecyclerViewAdapter(List<Game> games) {
        this.games = games;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.game_list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.game = games.get(position);
        holder.titleTextView.setText(games.get(position).getName());
        holder.ratingTextView.setText(String.format("%.2f", games.get(position).getRating()));
        Picasso.get()
               .load(IGDBImageUtils.getImageUrl(holder.game.getCover(), IGDBImageUtils.THUMB))
               .placeholder(R.drawable.ic_image_off_black_24dp)
               .into(holder.coverImage);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameDetailsIntent = new Intent(view.getContext(), GameDetailActivity.class);
                gameDetailsIntent.putExtra(GAME_EXTRA, holder.game);
                view.getContext().startActivity(gameDetailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @BindView(R.id.game_title)
        TextView titleTextView;
        @BindView(R.id.game_rating)
        TextView ratingTextView;
        @BindView(R.id.cover_image)
        ImageView coverImage;
        public Game game;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}
