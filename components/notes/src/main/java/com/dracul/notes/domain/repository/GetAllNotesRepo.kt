package com.dracul.notes.domain.repository

import com.dracul.notes.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface GetAllNotesRepo {
    operator fun invoke(): Flow<List<Note>>
}