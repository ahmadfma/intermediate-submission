package com.ahmadfma.intermediate_submission1.custom

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import com.ahmadfma.intermediate_submission1.R

class ProgressDialog(private val activity: Activity) {
    private var dialog: AlertDialog? = null

    @SuppressLint("InflateParams")
    fun startLoadingDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.progress_dialog, null))
        dialog = builder.create()
        dialog?.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }
}