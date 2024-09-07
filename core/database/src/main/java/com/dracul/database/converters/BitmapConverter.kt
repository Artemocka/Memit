package com.dracul.database.converters

import android.net.Uri
import androidx.room.TypeConverter

class UriConverter {

    @TypeConverter
    fun fromUri(uri:Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(uri:String): Uri {
        return Uri.parse(uri)
    }
}