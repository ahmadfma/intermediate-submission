package com.ahmadfma.intermediate_submission1.helper

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object FileHelper {

    private const val FILENAME_FORMAT = "dd-MMM-yyyy"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun createTempFile(context: Context): java.io.File {
        val storageDir: java.io.File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return java.io.File.createTempFile(timeStamp, ".jpg", storageDir)
    }

}