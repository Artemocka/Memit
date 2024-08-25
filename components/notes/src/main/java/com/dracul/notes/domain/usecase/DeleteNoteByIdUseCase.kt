package com.dracul.notes.domain.usecase

import com.dracul.notes.domain.repository.DeleteNoteByIdRepo

interface DeleteNoteByIdUseCase {
    operator fun invoke(id:Long)
}
class DeleteNoteByIdUseCaseImpl(
    val repo:DeleteNoteByIdRepo
):DeleteNoteByIdUseCase{
    override fun invoke(id: Long) {
        repo(id)
    }
}