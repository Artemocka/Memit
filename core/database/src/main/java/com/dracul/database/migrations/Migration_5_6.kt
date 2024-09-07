package com.dracul.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_5_6 = object : Migration(5,6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS `ImageEntity` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `parentId` INTEGER NOT NULL,
                `uri` TEXT NOT NULL
            )
            """.trimIndent()
        )
    }
}