package com.dracul.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dracul.database.notes.NoteDao
import com.dracul.database.notes.NoteEntity

@Database(
    entities = [
        NoteEntity::class,
    ],
    version = DatabaseProvider.VERSION
)
abstract class DatabaseProvider : RoomDatabase() {
    abstract val dao: NoteDao

    companion object {
        const val VERSION = 5
    }
}