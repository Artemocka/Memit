package com.dracul.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dracul.database.converters.UriConverter
import com.dracul.database.images.ImageEntity
import com.dracul.database.images.ImgageDao
import com.dracul.database.notes.NoteDao
import com.dracul.database.notes.NoteEntity

@Database(
    entities = [
        NoteEntity::class,
        ImageEntity::class
       ],
    version = DatabaseProvider.VERSION
)
@TypeConverters(UriConverter::class)
abstract class DatabaseProvider : RoomDatabase() {
    abstract val dao: NoteDao
    abstract val imageDao: ImgageDao

    companion object {
        const val VERSION = 6
    }
}