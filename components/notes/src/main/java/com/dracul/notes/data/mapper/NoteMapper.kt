package com.dracul.notes.data.mapper

import com.dracul.database.notes.NoteEntity
import com.dracul.notes.domain.models.Note


internal fun NoteEntity.toDomain(): Note = Note(
    id = id,
    title = title,
    content = content,
    color = color,
    pinned = pinned,
)

internal fun Note.toEntity(): NoteEntity =
    NoteEntity(
        id = id,
        title = title,
        content = content,
        color = color,
        pinned = pinned,
    )
