package com.dracul.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE NoteEntity_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                content TEXT NOT NULL,
                color INTEGER NOT NULL,
                pinned INTEGER NOT NULL,
                workerId TEXT,
                reminderTimeStamp INTEGER
            )
            """.trimIndent()
        )

        // Переносим данные из старой таблицы в новую
        database.execSQL(
            """
            INSERT INTO NoteEntity_new (id, title, content, color, pinned, workerId, reminderTimeStamp)
            SELECT id, title, content, color, pinned, workerId, 
                CASE 
                    WHEN reminderTimeStamp IS NOT NULL THEN CAST(reminderTimeStamp AS INTEGER)
                    ELSE NULL 
                END
            FROM NoteEntity
            """.trimIndent()
        )

        // Удаляем старую таблицу
        database.execSQL("DROP TABLE NoteEntity")

        // Переименовываем новую таблицу
        database.execSQL("ALTER TABLE NoteEntity_new RENAME TO NoteEntity")
    }
}