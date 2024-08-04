package com.dracul.notes.domain.usecase

import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.InsertNoteRepo

interface InsertNoteUseCase {
    operator fun invoke(item: Note)
}

class InsertNoteUseCaseImpl(
    val repo:InsertNoteRepo
): InsertNoteUseCase{
    override fun invoke(item: Note) {
        repo(item)
    }
}