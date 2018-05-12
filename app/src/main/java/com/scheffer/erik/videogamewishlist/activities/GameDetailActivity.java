package com.scheffer.erik.videogamewishlist.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.models.Game;
import com.scheffer.erik.videogamewishlist.models.Genre;
import com.scheffer.erik.videogamewishlist.models.Platform;
import com.scheffer.erik.videogamewishlist.models.Theme;
import com.scheffer.erik.videogamewishlist.recyclerviewadapters.VideoAdapter;
import com.scheffer.erik.videogamewishlist.utils.IGDBImageUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameDetailActivity extends AppCompatActivity {

    public static final String GAME_EXTRA = "game-extra";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar_cover)
    ImageView toolbarCover;
    @BindView(R.id.summary)
    TextView summaryTextView;
    @BindView(R.id.rating)
    TextView ratingTextView;
    @BindView(R.id.platforms)
    TextView platformsTextView;
    @BindView(R.id.genres)
    TextView genresTextView;
    @BindView(R.id.themes)
    TextView themesTextView;
    @BindView(R.id.videos_recyclerView)
    RecyclerView videosReciclerView;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Game game = getIntent().getParcelableExtra(GAME_EXTRA);

        if (game != null) {
            toolbar.setTitle(game.getName());
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            Picasso.get()
                   .load(IGDBImageUtils.getImageUrl(game.getCover(), IGDBImageUtils.SIZE_720P))
                   .placeholder(R.drawable.ic_image_off_black_24dp)
                   .into(toolbarCover);
            summaryTextView.setText(game.getSummary());
            ratingTextView.setText(String.format("%.2f", game.getRating()));

            StringBuilder platformsBuilder = new StringBuilder();
            for (Platform platform : game.getPlatforms()) {
                if (!platformsBuilder.toString().isEmpty()) {
                    platformsBuilder.append(", ");
                }
                platformsBuilder.append(platform.getName());
            }
            platformsTextView.setText(platformsBuilder.toString());

            StringBuilder genresBuilder = new StringBuilder();
            for (Genre genre : game.getGenres()) {
                if (!genresBuilder.toString().isEmpty()) {
                    genresBuilder.append(", ");
                }
                genresBuilder.append(genre.getName());
            }
            genresTextView.setText(genresBuilder.toString());

            StringBuilder themesBuilder = new StringBuilder();
            for (Theme theme : game.getThemes()) {
                if (!themesBuilder.toString().isEmpty()) {
                    themesBuilder.append(", ");
                }
                themesBuilder.append(theme.getName());
            }
            genresTextView.setText(themesBuilder.toString());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            videosReciclerView.setHasFixedSize(true);
            videosReciclerView.setLayoutManager(linearLayoutManager);
            videosReciclerView.addItemDecoration(
                    new DividerItemDecoration(this,
                                              linearLayoutManager.getOrientation()));
            videosReciclerView.setAdapter(new VideoAdapter(game.getVideos()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
