package com.dracul.notes.domain.repository

import com.dracul.notes.domain.models.Note

interface DuplicateNoteRepo {
    operator fun invoke(item:Note):Long
}