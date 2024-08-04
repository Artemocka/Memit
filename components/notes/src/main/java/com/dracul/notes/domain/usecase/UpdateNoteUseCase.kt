package com.dracul.notes.domain.usecase

import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.UpdateNoteRepo

interface UpdateNoteUseCase {
    operator fun invoke(item: Note)
}
class UpdateNoteUseCaseImpl(
    val repo:UpdateNoteRepo
):UpdateNoteUseCase {
    override fun invoke(item: Note) {
        repo(item)
    }
}