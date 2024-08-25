package com.dracul.notes.domain.repository

import com.dracul.notes.domain.models.Note

interface InsertNoteRepo {
    operator fun invoke(item: Note)
}