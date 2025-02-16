package com.dracul.task.domain.models

data class SubTask(
    val id: Int,
    val parentId: Int,
    val name: String,
    val isDone: Boolean,
)