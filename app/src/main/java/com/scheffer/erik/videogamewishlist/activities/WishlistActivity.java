package com.scheffer.erik.videogamewishlist.activities;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.converters.GameConverter;
import com.scheffer.erik.videogamewishlist.database.WishlistContract;
import com.scheffer.erik.videogamewishlist.models.Game;
import com.scheffer.erik.videogamewishlist.recyclerviewadapters.GameRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WishlistActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int GAMES_LOADER = 501;

    public static final String DATABASE_UPDATE_ACTION = "DATABASE_CHANGE";

    @BindView(R.id.wishlist_info_text)
    TextView infoText;
    @BindView(R.id.game_list)
    RecyclerView gamesRecyclerView;

    GameRecyclerViewAdapter gameRecyclerViewAdapter;
    BroadcastReceiver databaseChangeReceiver;

    private ArrayList<Game> games = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.search_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WishlistActivity.this, SearchFormActivity.class));
            }
        });

        infoText.setVisibility(View.VISIBLE);
        gamesRecyclerView.setVisibility(View.GONE);

        infoText.setText(R.string.loading_game_list);
        getLoaderManager().initLoader(GAMES_LOADER, null, this);

        databaseChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getLoaderManager().restartLoader(GAMES_LOADER, null, WishlistActivity.this);
            }
        };
        LocalBroadcastManager.getInstance(this)
                             .registerReceiver(databaseChangeReceiver,
                                               new IntentFilter(DATABASE_UPDATE_ACTION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wishlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                                Uri.withAppendedPath(WishlistContract.BASE_CONTENT_URI,
                                                     WishlistContract.PATH_GAMES),
                                null,
                                null,
                                null,
                                WishlistContract.GameEntry.COLUMN_RATING + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        games.clear();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            games.add(GameConverter.fromCursor(cursor));
        }

        if (games.isEmpty()) {
            infoText.setText(R.string.no_games_found);
            infoText.setVisibility(View.VISIBLE);
            gamesRecyclerView.setVisibility(View.GONE);
        } else {
            gamesRecyclerView.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.GONE);

            if (gameRecyclerViewAdapter != null) {
                gameRecyclerViewAdapter.notifyDataSetChanged();
            } else {
                gamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                gameRecyclerViewAdapter = new GameRecyclerViewAdapter(games);
                gamesRecyclerView.setAdapter(gameRecyclerViewAdapter);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(databaseChangeReceiver);
        super.onDestroy();
    }
}
