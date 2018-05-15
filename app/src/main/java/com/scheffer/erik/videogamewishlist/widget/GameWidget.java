package com.scheffer.erik.videogamewishlist.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.scheffer.erik.videogamewishlist.R;
import com.scheffer.erik.videogamewishlist.converters.GameConverter;
import com.scheffer.erik.videogamewishlist.database.WishlistContract;
import com.scheffer.erik.videogamewishlist.models.Game;
import com.scheffer.erik.videogamewishlist.utils.IGDBImageUtils;
import com.squareup.picasso.Picasso;

public class GameWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.game_widget);

        Cursor randomGameCursor = context.getContentResolver()
                                         .query(WishlistContract.GameEntry.CONTENT_URI.buildUpon()
                                                                                      .appendPath(
                                                                                              WishlistContract.PATH_RANDOM)
                                                                                      .build(),
                                                null,
                                                null,
                                                null,
                                                null);
        if (randomGameCursor != null) {
            if (randomGameCursor.getCount() > 0) {
                randomGameCursor.moveToFirst();
                Game game = GameConverter.fromCursor(randomGameCursor);
                if (game.getCover() != null) {
                    Picasso.get()
                           .load(IGDBImageUtils.getImageUrl(game.getCover(),
                                                            IGDBImageUtils.SIZE_720P))
                           .into(views, R.id.widget_cover, new int[]{appWidgetId});
                }
            }
            randomGameCursor.close();
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
