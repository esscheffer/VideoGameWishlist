package com.scheffer.erik.videogamewishlist.activities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.converters.GameConverter;
import com.scheffer.erik.videogamewishlist.converters.PlatformConverter;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.GameEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.GamePlatformEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistContract.PlatformEntry;
import com.scheffer.erik.videogamewishlist.database.WishlistDbHelper;
import com.scheffer.erik.videogamewishlist.fragments.GameListFragment;
import com.scheffer.erik.videogamewishlist.models.Game;
import com.scheffer.erik.videogamewishlist.models.Genre;
import com.scheffer.erik.videogamewishlist.models.Image;
import com.scheffer.erik.videogamewishlist.models.Platform;
import com.scheffer.erik.videogamewishlist.models.Theme;
import com.scheffer.erik.videogamewishlist.models.Video;
import com.scheffer.erik.videogamewishlist.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scheffer.erik.videogamewishlist.fragments.GameListFragment.GAMES_LIST_KEY;

public class WishlistActivity extends AppCompatActivity {
    @BindView(R.id.create_button)
    Button createButton;
    @BindView(R.id.delete_button)
    Button deleteButton;

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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        GameListFragment gameListFragment = new GameListFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments

        ArrayList<Game> games = new ArrayList<>();
        games.add(TestUtils.getMockGame());
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putParcelableArrayList(GAMES_LIST_KEY, games);
        gameListFragment.setArguments(fragmentArgs);

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.fragment_container, gameListFragment).commit();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                databaseInsert();
                contentProviderInsert();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WishlistDbHelper dbHelper = new WishlistDbHelper(WishlistActivity.this);
                final SQLiteDatabase database = dbHelper.getWritableDatabase();

                int deleted = database.delete(GameEntry.TABLE_NAME,
                                              GameEntry.COLUMN_EXTERNAL_ID + "=?",
                                              new String[]{String.valueOf(0)});
                System.out.println(deleted);
            }
        });
    }

    private void databaseInsert() {
        int platformExternalId = 15;

        WishlistDbHelper dbHelper = new WishlistDbHelper(WishlistActivity.this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues gameValues = new ContentValues();
        gameValues.put(GameEntry.COLUMN_EXTERNAL_ID, 0);
        gameValues.put(GameEntry.COLUMN_NAME, "title");
        gameValues.put(GameEntry.COLUMN_SUMMARY, "sumary");
        gameValues.put(GameEntry.COLUMN_RATING, 0);
        long newGameId = database.insert(GameEntry.TABLE_NAME, null, gameValues);

        ContentValues platformValues = new ContentValues();
        platformValues.put(PlatformEntry.COLUMN_EXTERNAL_ID, platformExternalId);
        platformValues.put(PlatformEntry.COLUMN_NAME, "platform name");
        long newPlatformId = database.insert(PlatformEntry.TABLE_NAME, null, platformValues);

        ContentValues gamePlatformValues = new ContentValues();
        gamePlatformValues.put(GamePlatformEntry.COLUMN_GAME_ID, newGameId);
        gamePlatformValues.put(GamePlatformEntry.COLUMN_PLATFORM_EXTERNAL_ID,
                               platformExternalId);
        long newGamePlatformId = database.insert(GamePlatformEntry.TABLE_NAME,
                                                 null,
                                                 gamePlatformValues);
    }

    private void contentProviderInsert() {
        Game game = new Game();
        game.setName("name");
        game.setSummary("summary");
        game.setRating(5.5);

        Image cover = new Image();
        cover.setUrl("url");
        cover.setCloudinaryId("id");
        game.setCover(cover);

        Platform p1 = new Platform();
        p1.setId(1);
        p1.setName("p1");
        Platform p2 = new Platform();
        p2.setId(2);
        p2.setName("p2");
        List<Platform> platforms = new ArrayList<>();
        platforms.add(p1);
        platforms.add(p2);
        game.setPlatforms(platforms);

        Uri platformUri = getContentResolver().insert(PlatformEntry.CONTENT_URI,
                                                      PlatformConverter.toContentValues(p1));
        long platformId = ContentUris.parseId(platformUri);

        Genre g1 = new Genre();
        g1.setId(1);
        g1.setName("g1");
        Genre g2 = new Genre();
        g2.setId(2);
        g2.setName("g2");
        List<Genre> genres = new ArrayList<>();
        genres.add(g1);
        genres.add(g2);
        game.setGenres(genres);

        Theme t1 = new Theme();
        t1.setId(1);
        t1.setName("t1");
        Theme t2 = new Theme();
        t2.setId(2);
        t2.setName("t2");
        List<Theme> themes = new ArrayList<>();
        themes.add(t1);
        themes.add(t2);
        game.setThemes(themes);

        Video v1 = new Video();
        v1.setName("v1");
        v1.setVideoId("id1");
        Video v2 = new Video();
        v2.setName("v2");
        v2.setVideoId("id2");
        List<Video> videos = new ArrayList<>();
        videos.add(v1);
        videos.add(v2);
        game.setVideos(videos);

        Uri uri = getContentResolver().insert(GameEntry.CONTENT_URI,
                                              GameConverter.toContentValues(game));
        long gameId = ContentUris.parseId(uri);
        System.out.println(gameId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wishlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
