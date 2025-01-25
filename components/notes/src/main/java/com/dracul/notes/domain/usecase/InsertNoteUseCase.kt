package com.dracul.notes.domain.usecase

import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.InsertNoteRepo

interface InsertNoteUseCase {
    operator fun invoke(item: Note): Long
}

class InsertNoteUseCaseImpl(
    val repository: InsertNoteRepo
) : InsertNoteUseCase {

    override fun invoke(item: Note): Long = repository(item)

}