package com.ahmadfma.intermediate_submission1.helper

import java.text.SimpleDateFormat
import java.util.*

fun String.convertToDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val timeFormat = SimpleDateFormat("HH:mm aaa", Locale.US)
    val outputFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL)
    val date = inputFormat.parse(this) as Date
    return outputFormat.format(date) + " " + timeFormat.format(date)
}