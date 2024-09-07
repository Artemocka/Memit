package com.dracul.common.utills

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream


fun copyUriToInternalStorage(context: Context, uri: Uri, fileName: String): Uri? {
    val picturesDir = File(context.filesDir, "pictures")
    if (!picturesDir.exists()) {
        picturesDir.mkdirs()
    }

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


fun createAppDirectory(context: Context, directoryName: String): File? {
    val appDirectory = File(context.filesDir, directoryName)

    if (!appDirectory.exists()) {
        val isCreated = appDirectory.mkdirs()
        if (isCreated) {
            println("Directory created: ${appDirectory.absolutePath}")
        } else {
            println("Failed to create directory: ${appDirectory.absolutePath}")
        }
    } else {
        println("Directory already exists: ${appDirectory.absolutePath}")
    }

    return if (appDirectory.exists()) appDirectory else null
}