package com.dracul.notes.domain.models

import androidx.compose.runtime.Immutable
import androidx.room.PrimaryKey
import com.dracul.common.models.Card

@Immutable
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val content: String,
    override val color: Int,
    override val pinned: Boolean = false,
    override val workerId: String? = null,
    override val reminderTimeStamp: Long? = null,
) : Card