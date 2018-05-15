package com.scheffer.erik.videogamewishlist.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.converters.GameConverter;
import com.scheffer.erik.videogamewishlist.database.WishlistContract;
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
    RecyclerView videosRecyclerView;
    @BindView(R.id.adView)
    AdView adView;

    private boolean isSaved = false;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        ButterKnife.bind(this);

        MobileAds.initialize(this, "ca-app-pub-8540885734073963~9962865212");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        final Game game = getIntent().getParcelableExtra(GAME_EXTRA);

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

            if (game.getVideos() != null) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                videosRecyclerView.setHasFixedSize(true);
                videosRecyclerView.setLayoutManager(linearLayoutManager);
                videosRecyclerView.addItemDecoration(
                        new DividerItemDecoration(this,
                                                  linearLayoutManager.getOrientation()));
                videosRecyclerView.setAdapter(new VideoAdapter(game.getVideos()));
            }

            Cursor cursor = getContentResolver()
                    .query(WishlistContract.GameEntry.CONTENT_URI.buildUpon()
                                                                 .appendPath(
                                                                         String.valueOf(game.getId()))
                                                                 .build(),
                           null,
                           null,
                           null,
                           null);
            if (cursor != null && cursor.getCount() > 0) {
                setSaved(true);
                cursor.close();
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSaved) {
                    getContentResolver()
                            .delete(WishlistContract.GameEntry.CONTENT_URI.buildUpon()
                                                                          .appendPath(
                                                                                  String.valueOf(
                                                                                          game.getId()))
                                                                          .build(),
                                    null,
                                    null);
                    setSaved(false);
                } else {
                    getContentResolver().insert(WishlistContract.GameEntry.CONTENT_URI,
                                                GameConverter.toContentValues(game));
                    setSaved(true);
                }
                LocalBroadcastManager.getInstance(GameDetailActivity.this)
                                     .sendBroadcast(new Intent(WishlistActivity.DATABASE_UPDATE_ACTION));
            }
        });
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

    private void setSaved(boolean isSaved) {
        this.isSaved = isSaved;
        if (this.isSaved) {
            fab.setImageDrawable(ContextCompat.getDrawable(this,
                                                           R.drawable.ic_favorite_black_24dp));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(this,
                                                           R.drawable.ic_favorite_border_black_24dp));
        }
    }
}
