package com.dracul.notes.domain.repository

import com.dracul.notes.domain.models.Note

interface UpdateNoteRepo {
    operator fun invoke(item: Note)
}