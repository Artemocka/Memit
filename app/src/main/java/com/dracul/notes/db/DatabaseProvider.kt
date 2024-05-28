package com.dracul.notes.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Note::class,
    ],
    version = DatabaseProvider.VERSION
)
abstract class DatabaseProvider : RoomDatabase() {
    abstract val dao: NoteDao

    companion object {
        const val VERSION = 1
    }
}