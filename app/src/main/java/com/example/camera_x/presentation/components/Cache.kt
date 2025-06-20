package com.example.camera_x.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val cachePath = File(context.cacheDir, "filtered_images")
    cachePath.mkdirs()

    val file = File(cachePath, "filtered_${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    // Use FileProvider to get a content Uri
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // you must define this in manifest
        file
    )
}
