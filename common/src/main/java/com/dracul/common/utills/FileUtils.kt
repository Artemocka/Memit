package com.dracul.common.utills

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream


fun copyUriToInternalStorage(context: Context, uri: Uri, fileName: String): Uri? {
    val picturesDir = File(context.filesDir, "pictures")
    if (!picturesDir.exists()) picturesDir.mkdirs()
    val file = File(picturesDir, fileName)
    try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file.toUri()
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}