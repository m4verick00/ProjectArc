package com.arclogbook.ui.widget

import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import com.arclogbook.R

class QuickLogWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_quick_log)
            // Add logic for quick log entry
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
