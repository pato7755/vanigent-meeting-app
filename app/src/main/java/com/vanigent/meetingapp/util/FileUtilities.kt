package com.vanigent.meetingapp.util

import android.content.Context
import android.graphics.Bitmap
import com.vanigent.meetingapp.util.DateUtilities.getCurrentDate
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

object FileUtilities {

    fun storeBitmapImage(
        context: Context,
        fileName: String,
        imageBitmap: Bitmap
    ): String {

        // Access the application's internal storage directory
        val directory = File(context.filesDir, getCurrentDate())
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName)

        return try {
            val fos = FileOutputStream(file)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            Timber.d(file.relativeTo(context.filesDir).path)
            file.relativeTo(context.filesDir).path
        } catch (e: Exception) {
            println("exception - ${e.localizedMessage}")
            e.printStackTrace()
            ""
        }

    }

}