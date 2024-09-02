package com.dracul.common.utills

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object BitmapConverter {
    fun converterBitmapToString(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)

        return baos.toByteArray()
    }

    fun converterStringToBitmap(encodedByte: ByteArray): Bitmap? {
        return try {
            BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}