package com.scheffer.erik.videogamewishlist.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.appyvet.materialrangebar.RangeBar;
import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.converters.GenreConverter;
import com.scheffer.erik.videogamewishlist.converters.PlatformConverter;
import com.scheffer.erik.videogamewishlist.converters.ThemeConverter;
import com.scheffer.erik.videogamewishlist.database.WishlistContract;
import com.scheffer.erik.videogamewishlist.models.Genre;
import com.scheffer.erik.videogamewishlist.models.Platform;
import com.scheffer.erik.videogamewishlist.models.Theme;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFormActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PLATFORMS_LOADER = 101;
    private static final int GENRES_LOADER = 102;
    private static final int THEMES_LOADER = 103;

    @BindView(R.id.title_inputText)
    TextInputEditText titleInputText;
    @BindView(R.id.platforms_spinner)
    SearchableSpinner platformsSpinner;
    @BindView(R.id.genres_spinner)
    SearchableSpinner genresSpinner;
    @BindView(R.id.themes_spinner)
    SearchableSpinner themesSpinner;
    @BindView(R.id.rating_rangeBar)
    RangeBar ratingRangeBar;
    @BindView(R.id.search_button)
    Button searchButton;

    private List<Platform> platforms = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
    private List<Theme> themes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_form);
        ButterKnife.bind(this);

        platformsSpinner.setTitle(getResources().getString(R.string.select_platform));
        genresSpinner.setTitle(getResources().getString(R.string.select_genre));
        themesSpinner.setTitle(getResources().getString(R.string.select_theme));

        getLoaderManager().initLoader(PLATFORMS_LOADER, null, this);
        getLoaderManager().initLoader(GENRES_LOADER, null, this);
        getLoaderManager().initLoader(THEMES_LOADER, null, this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gameTitle = titleInputText.getText().toString();

                Platform selectedPlatform = null;
                if (platformsSpinner.getSelectedItemPosition() > 0) {
                    selectedPlatform = platforms.get(platformsSpinner.getSelectedItemPosition() - 1);
                }

                Genre selectedGenre = null;
                if (genresSpinner.getSelectedItemPosition() > 0) {
                    selectedGenre = genres.get(genresSpinner.getSelectedItemPosition() - 1);
                }

                Theme selectedTheme = null;
                if (themesSpinner.getSelectedItemPosition() > 0) {
                    selectedTheme = themes.get(themesSpinner.getSelectedItemPosition() - 1);
                }

                int minimumRating = ratingRangeBar.getLeftIndex();
                int maximumRating = ratingRangeBar.getRightIndex();

                Intent searchListIntent = new Intent(SearchFormActivity.this,
                                                     GameSearchListActivity.class);
                searchListIntent.putExtra(GameSearchListActivity.GAME_TITLE_EXTRA, gameTitle);
                searchListIntent.putExtra(GameSearchListActivity.PLATFORM_EXTRA, selectedPlatform);
                searchListIntent.putExtra(GameSearchListActivity.GENRE_EXTRA, selectedGenre);
                searchListIntent.putExtra(GameSearchListActivity.THEME_EXTRA, selectedTheme);
                searchListIntent.putExtra(GameSearchListActivity.MINIMUM_RATING_EXTRA,
                                          minimumRating);
                searchListIntent.putExtra(GameSearchListActivity.MAXIMUM_RATING_EXTRA,
                                          maximumRating);

                startActivity(searchListIntent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri searchUri;
        String orderColumn;
        switch (id) {
            case PLATFORMS_LOADER:
                searchUri = Uri.withAppendedPath(WishlistContract.BASE_CONTENT_URI,
                                                 WishlistContract.PATH_PLATFORMS);
                orderColumn = WishlistContract.PlatformEntry.COLUMN_NAME;
                break;
            case GENRES_LOADER:
                searchUri = Uri.withAppendedPath(WishlistContract.BASE_CONTENT_URI,
                                                 WishlistContract.PATH_GENRES);
                orderColumn = WishlistContract.GenreEntry.COLUMN_NAME;
                break;
            case THEMES_LOADER:
                searchUri = Uri.withAppendedPath(WishlistContract.BASE_CONTENT_URI,
                                                 WishlistContract.PATH_THEMES);
                orderColumn = WishlistContract.ThemeEntry.COLUMN_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unkow loader id of: " + id);
        }
        return new CursorLoader(this, searchUri, null, null, null, orderColumn);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case PLATFORMS_LOADER:
                platforms = new ArrayList<>();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    platforms.add(PlatformConverter.fromCursor(cursor));
                }
                List<String> platformsNames = new ArrayList<>();
                platformsNames.add(getResources().getString(R.string.any_platform));
                for (Platform platform : platforms) {
                    platformsNames.add(platform.getName());
                }
                ArrayAdapter<String> platformsAdapter
                        = new ArrayAdapter<>(this,
                                             android.R.layout.simple_spinner_item,
                                             platformsNames);
                platformsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                platformsSpinner.setAdapter(platformsAdapter);
                break;
            case GENRES_LOADER:
                genres = new ArrayList<>();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    genres.add(GenreConverter.fromCursor(cursor));
                }
                List<String> genresNames = new ArrayList<>();
                genresNames.add(getResources().getString(R.string.any_genre));
                for (Genre genre : genres) {
                    genresNames.add(genre.getName());
                }
                ArrayAdapter<String> genresAdapter
                        = new ArrayAdapter<>(this,
                                             android.R.layout.simple_spinner_item,
                                             genresNames);
                genresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                genresSpinner.setAdapter(genresAdapter);
                break;
            case THEMES_LOADER:
                themes = new ArrayList<>();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    themes.add(ThemeConverter.fromCursor(cursor));
                }
                List<String> themesNames = new ArrayList<>();
                themesNames.add(getResources().getString(R.string.any_theme));
                for (Theme theme : themes) {
                    themesNames.add(theme.getName());
                }
                ArrayAdapter<String> themesAdapter
                        = new ArrayAdapter<>(this,
                                             android.R.layout.simple_spinner_item,
                                             themesNames);
                themesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                themesSpinner.setAdapter(themesAdapter);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
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