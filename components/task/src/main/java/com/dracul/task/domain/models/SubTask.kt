package com.dracul.task.domain.models

import androidx.room.PrimaryKey

data class SubTask(
    @PrimaryKey
    val id: Int,
    val parentId: Int,
)