package com.dracul.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dracul.database.converters.UriConverter
import com.dracul.database.images.ImageEntity
import com.dracul.database.images.ImgageDao
import com.dracul.database.notes.NoteDao
import com.dracul.database.notes.NoteEntity
import com.dracul.database.sub_tasks.SubTaskDao
import com.dracul.database.sub_tasks.SubTaskEntity
import com.dracul.database.tasks.TaskDao
import com.dracul.database.tasks.TaskEntity

@Database(
    entities = [
        NoteEntity::class,
        ImageEntity::class,
        TaskEntity::class,
        SubTaskEntity::class,
       ],
    version = DatabaseProvider.VERSION,
    exportSchema = false,
)
@TypeConverters(UriConverter::class)
abstract class DatabaseProvider : RoomDatabase() {

    abstract val dao: NoteDao
    abstract val imageDao: ImgageDao
    abstract val taskDao: TaskDao
    abstract val subTaskDao: SubTaskDao

    companion object {
        const val VERSION = 7
    }
}