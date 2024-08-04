package com.dracul.notes.domain.repository

import com.dracul.notes.domain.models.Note

interface DeleteNoteRepo {
    operator fun invoke(item: Note)
}