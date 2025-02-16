package com.dracul.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `TaskEntity` (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                color INTEGER NOT NULL,
                pinned INTEGER NOT NULL,
                workerId TEXT,
                reminderTimeStamp INTEGER
            )
            """.trimIndent()
        )
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `SubTaskEntity` (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                parentId INTEGER NOT NULL,
                name TEXT NOT NULL,
                isDone INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}