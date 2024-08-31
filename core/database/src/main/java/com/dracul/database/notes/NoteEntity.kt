package com.dracul.database.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NoteEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val content: String,
    val color: Int,
    val pinned: Boolean = false,
    val workerId:String?,
    val reminderTimeStamp:Long?,
)