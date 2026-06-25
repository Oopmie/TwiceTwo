package com.example.twicetwo.presentation.activity

import android.content.Context
import android.net.Uri
import java.io.File

fun saveImageToInternalStorage(context: Context, uri: Uri): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.filesDir, "img_${System.currentTimeMillis()}.jpg")

    file.outputStream().use { output ->
        inputStream?.copyTo(output)
    }

    return file.absolutePath
}