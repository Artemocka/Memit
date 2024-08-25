package com.dracul.notes.domain.usecase

import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.DeleteNoteRepo

interface DeleteNoteUseCase {
    operator fun invoke(item: Note)
}

class DeleteNoteUseCaseImpl(
    val repo: DeleteNoteRepo
) : DeleteNoteUseCase {
    override fun invoke(item: Note) {
        repo(item)
    }
}