package com.dracul.database.tasks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var title: String,
    val color: Int,
    val pinned: Boolean = false,
    val workerId: String? = null,
    val reminderTimeStamp: Long? = null,
)