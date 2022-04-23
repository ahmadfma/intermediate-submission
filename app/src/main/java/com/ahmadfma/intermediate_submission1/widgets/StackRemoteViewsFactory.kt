package com.ahmadfma.intermediate_submission1.widgets

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.ahmadfma.intermediate_submission1.R
import com.bumptech.glide.Glide
import java.lang.Exception
import java.util.ArrayList

internal class StackRemoteViewsFactory(private val context: Context): RemoteViewsService.RemoteViewsFactory {

    companion object {
        var mWidgetItems = ArrayList<Uri>()
    }

    private var items = ArrayList<Uri>()

    override fun onDataSetChanged() {
        Log.d("StackRemoteViewsFactory", "onDataSetChanged: ${mWidgetItems.size}")
        if(mWidgetItems.isNotEmpty()) {
            items.clear()
            mWidgetItems.forEach {
                items.add(it)
            }
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        val bitmap : Bitmap = try {
            Glide.with(context)
                .asBitmap()
                .load(items[position])
                .submit(512, 512)
                .get()
        } catch (e : Exception) {
            Glide.with(context)
                .asBitmap()
                .load(R.drawable.talk)
                .submit(512, 512)
                .get()
        }
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

    override fun getCount(): Int = items.size

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}