package com.dracul.database.sub_tasks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class SubTaskEntity(
    @PrimaryKey
    val id: Int,
    val parentId: Int,
    val name: String,
    val isDone: Boolean,
)