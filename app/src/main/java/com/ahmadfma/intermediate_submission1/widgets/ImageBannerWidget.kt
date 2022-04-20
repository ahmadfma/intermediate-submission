package com.ahmadfma.intermediate_submission1.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.data.model.GetStoryResponse

class ImageBannerWidget : AppWidgetProvider() {

    companion object {
        const val EXTRA_ITEM = "com.ahmadfma.intermediate_submission1.EXTRA_ITEM"
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val intent = Intent(context, StackWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
        val views = RemoteViews(context.packageName, R.layout.image_banner_widget)
        views.setRemoteAdapter(R.id.stack_view, intent)
        views.setEmptyView(R.id.stack_view, R.id.empty_view)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val extra = intent.getParcelableExtra<GetStoryResponse>(EXTRA_ITEM)
        if(extra != null) {
            StackRemoteViewsFactory.mWidgetItems = arrayListOf()
            extra.listStory?.forEach {
                StackRemoteViewsFactory.mWidgetItems.add(Uri.parse(it.photoUrl))
            }
            val intent2 = Intent(context, StackWidgetService::class.java)
            intent2.data = intent2.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val views = RemoteViews(context.packageName, R.layout.image_banner_widget)
            views.setRemoteAdapter(R.id.stack_view, intent2)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)
            val manager = AppWidgetManager.getInstance(context)
            manager.updateAppWidget(ComponentName(context, ImageBannerWidget::class.java), views)
        }
    }

    override fun onEnabled(context: Context) {}

    override fun onDisabled(context: Context) {}
}