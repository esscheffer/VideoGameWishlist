package com.scheffer.erik.videogamewishlist.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scheffer.erik.videogamewishlist.BuildConfig;
import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.database.WishlistContract;
import com.scheffer.erik.videogamewishlist.fragments.GameListFragment;
import com.scheffer.erik.videogamewishlist.models.Game;
import com.scheffer.erik.videogamewishlist.models.Genre;
import com.scheffer.erik.videogamewishlist.models.IGDBGame;
import com.scheffer.erik.videogamewishlist.models.Platform;
import com.scheffer.erik.videogamewishlist.models.Theme;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import callback.OnSuccessCallback;
import wrapper.IGDBWrapper;
import wrapper.Parameters;
import wrapper.Version;

import static com.scheffer.erik.videogamewishlist.fragments.GameListFragment.GAMES_LIST_KEY;

public class GameSearchListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_list_info_text)
    TextView infoText;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        fragmentLayout.setVisibility(View.GONE);
        infoText.setVisibility(View.VISIBLE);

        infoText.setText(R.string.loading_game_list);

        final IGDBWrapper wrapper = new IGDBWrapper(BuildConfig.API_KEY,
                                                    Version.STANDARD,
                                                    false);

        Parameters params = new Parameters()
                .addFields("*")
                .addFilter("[rating][gte]=80")
//                .addFilter("[platforms][eq]=" + platform)
                .addLimit("5");

        wrapper.games(params, new OnSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {

                Type listType = new TypeToken<ArrayList<IGDBGame>>() {}.getType();
                ArrayList<IGDBGame> fromGson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create().fromJson(result.toString(), listType);


                final ArrayList<Game> games = new ArrayList<>();
                for (IGDBGame igdbGame : fromGson) {
                    Game game = new Game();
                    game.setId(igdbGame.getId());
                    game.setName(igdbGame.getName());
                    game.setSummary(igdbGame.getSummary());
                    game.setRating(igdbGame.getRating());
                    game.setCover(igdbGame.getCover());
                    game.setVideos(igdbGame.getVideos());

                    List<Platform> platforms = new ArrayList<>();
                    if (igdbGame.getPlatforms() != null) {
                        for (Long platformId : igdbGame.getPlatforms()) {
                            Cursor cursor = getContentResolver()
                                    .query(WishlistContract.PlatformEntry.CONTENT_URI.buildUpon()
                                                                                     .appendPath(
                                                                                             String.valueOf(
                                                                                                     platformId))
                                                                                     .build(),
                                           null,
                                           null,
                                           null,
                                           null);
                            if (cursor != null) {
                                if (cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    platforms.add(new Platform(platformId,
                                                               cursor.getString(cursor.getColumnIndex(
                                                                       WishlistContract.PlatformEntry.COLUMN_NAME))));
                                }
                                cursor.close();
                            }
                        }
                    }
                    game.setPlatforms(platforms);

                    List<Genre> genres = new ArrayList<>();
                    if (igdbGame.getGenres() != null) {
                        for (Long genreId : igdbGame.getGenres()) {
                            Cursor cursor = getContentResolver()
                                    .query(WishlistContract.GenreEntry.CONTENT_URI.buildUpon()
                                                                                  .appendPath(
                                                                                          String.valueOf(
                                                                                                  genreId))
                                                                                  .build(),
                                           null,
                                           null,
                                           null,
                                           null);
                            if (cursor != null) {
                                if (cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    genres.add(new Genre(genreId,
                                                         cursor.getString(cursor.getColumnIndex(
                                                                 WishlistContract.ThemeEntry.COLUMN_NAME))));
                                }
                                cursor.close();
                            }
                        }
                    }
                    game.setGenres(genres);

                    List<Theme> themes = new ArrayList<>();
                    if (igdbGame.getThemes() != null) {
                        for (Long themeId : igdbGame.getThemes()) {
                            Cursor cursor = getContentResolver()
                                    .query(WishlistContract.ThemeEntry.CONTENT_URI.buildUpon()
                                                                                  .appendPath(
                                                                                          String.valueOf(
                                                                                                  themeId))
                                                                                  .build(),
                                           null,
                                           null,
                                           null,
                                           null);
                            if (cursor != null) {
                                if (cursor.getCount() > 0) {
                                    cursor.moveToFirst();
                                    themes.add(new Theme(themeId,
                                                         cursor.getString(cursor.getColumnIndex(
                                                                 WishlistContract.ThemeEntry.COLUMN_NAME))));
                                }
                                cursor.close();
                            }
                        }
                    }
                    game.setThemes(themes);

                    games.add(game);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (games.isEmpty()) {
                            infoText.setText(R.string.no_games_found);
                        }
                        GameListFragment gameListFragment = new GameListFragment();
                        Bundle fragmentArgs = new Bundle();
                        fragmentArgs.putParcelableArrayList(GAMES_LIST_KEY, games);
                        gameListFragment.setArguments(fragmentArgs);


                        fragmentLayout.setVisibility(View.VISIBLE);
                        infoText.setVisibility(View.GONE);

                        getSupportFragmentManager().beginTransaction()
                                                   .add(R.id.fragment_container, gameListFragment)
                                                   .commit();
                    }
                });


            }

            @Override
            public void onError(Exception error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            infoText.setText(R.string.error_games_from_server);
                    }
                });
                error.printStackTrace();
            }
        });
    }
}
