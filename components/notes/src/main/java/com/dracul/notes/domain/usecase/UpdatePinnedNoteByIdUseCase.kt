package com.dracul.notes.domain.usecase

import com.dracul.notes.domain.repository.UpdatePinnedNoteByIdRepo

interface UpdatePinnedNoteByIdUseCase {
    operator fun invoke(id: Long, pinned: Boolean)
}

class UpdatePinnedNoteByIdUseCaseImpl(
    val repo: UpdatePinnedNoteByIdRepo
) : UpdatePinnedNoteByIdUseCase {
    override fun invoke(id: Long, pinned: Boolean) {
        repo(id, pinned)
    }
}