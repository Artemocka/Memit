package com.dracul.database.db

import android.content.Context
import androidx.room.Room
import com.dracul.database.migrations.MIGRATION_1_2
import com.dracul.database.migrations.MIGRATION_2_3
import com.dracul.database.notes.NoteDao

object DatabaseProviderWrap {

    const val VERSION = DatabaseProvider.VERSION
    private lateinit var provider: DatabaseProvider


    val noteDao: NoteDao get() = provider.dao

    fun closeDao() = provider.close()


    fun createDao(context: Context) {
        provider = Room.databaseBuilder(context, DatabaseProvider::class.java, "notes")
            .allowMainThreadQueries()
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }
}