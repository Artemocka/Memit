package com.dracul.database.db

import android.content.Context
import androidx.room.Room
import com.dracul.database.images.ImgageDao
import com.dracul.database.migrations.MIGRATION_1_2
import com.dracul.database.migrations.MIGRATION_2_3
import com.dracul.database.migrations.MIGRATION_3_4
import com.dracul.database.migrations.MIGRATION_4_5
import com.dracul.database.migrations.MIGRATION_5_6
import com.dracul.database.notes.NoteDao

object DatabaseProviderWrap {

    const val VERSION = DatabaseProvider.VERSION
    private lateinit var provider: DatabaseProvider


    val noteDao: NoteDao get() = provider.dao
    val imageDao: ImgageDao get() = provider.imageDao

    fun closeDao() = provider.close()


    fun createDao(context: Context) {
        provider = Room.databaseBuilder(context, DatabaseProvider::class.java, "notes")
            .allowMainThreadQueries()
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5,MIGRATION_5_6)
            .build()
    }
}