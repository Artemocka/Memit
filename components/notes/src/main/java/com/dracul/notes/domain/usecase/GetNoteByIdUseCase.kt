package com.dracul.notes.domain.usecase

import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.GetNoteByIdRepo

interface GetNoteByIdUseCase {
    operator fun invoke(id:Long): Note
}

class GetNoteByIdUseCaseImpl(
    val repo:GetNoteByIdRepo
):GetNoteByIdUseCase{
    override fun invoke(id:Long): Note {
        return repo(id)
    }
}