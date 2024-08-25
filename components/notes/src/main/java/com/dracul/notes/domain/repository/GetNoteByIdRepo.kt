package com.dracul.notes.domain.repository

import com.dracul.notes.domain.models.Note

interface GetNoteByIdRepo {
    operator fun invoke(id:Long): Note
}