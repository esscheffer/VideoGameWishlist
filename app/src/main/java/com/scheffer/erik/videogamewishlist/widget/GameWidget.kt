package com.scheffer.erik.videogamewishlist.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.scheffer.erik.videogamewishlist.R
import com.scheffer.erik.videogamewishlist.database.getAllGames
import com.scheffer.erik.videogamewishlist.utils.IGDBImageUtils
import com.scheffer.erik.videogamewishlist.utils.random
import com.squareup.picasso.Picasso

class GameWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            val views = RemoteViews(context.packageName, R.layout.game_widget)

            getAllGames().random()?.cover?.let { cover ->
                Picasso.get()
                        .load(IGDBImageUtils.getImageUrl(cover,
                                IGDBImageUtils.SIZE_720P))
                        .into(views, R.id.widget_cover, intArrayOf(appWidgetId))
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
