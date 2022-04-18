package com.ahmadfma.intermediate_submission1.widgets

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.ahmadfma.intermediate_submission1.R
import com.bumptech.glide.Glide
import java.util.ArrayList

internal class StackRemoteViewsFactory(private val context: Context): RemoteViewsService.RemoteViewsFactory {

    companion object {
        var mWidgetItems = ArrayList<Uri>()
    }

    override fun onDataSetChanged() {
        Log.d("StackRemoteViewsFactory", "onDataSetChanged: $mWidgetItems")
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)

        val bitmap = Glide.with(context)
            .asBitmap()
            .load(mWidgetItems[position])
            .submit(512, 512)
            .get()

        rv.setImageViewBitmap(R.id.imageView, bitmap)
        val extras = bundleOf(
            ImageBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun onCreate() {}

    override fun onDestroy() {}

    override fun getCount(): Int = mWidgetItems.size

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}