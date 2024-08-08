package com.example.bookstore.common

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val file = File(context.filesDir, "profile_${System.currentTimeMillis()}.jpg")

    inputStream.use { input ->
        FileOutputStream(file).use { output ->
            input?.copyTo(output)
        }
    }
    return file.absolutePath
}
