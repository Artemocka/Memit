package com.dracul.notes.domain.models

import androidx.compose.runtime.Immutable
import androidx.room.PrimaryKey

@Immutable
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val content: String,
    val color: Int,
    val pinned: Boolean = false,
    val workerId: String? = null,
)