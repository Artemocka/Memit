package com.dracul.notes.domain.usecase

import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.GetAllNotesRepo
import kotlinx.coroutines.flow.Flow

interface GetAllNotesUseCase {
    operator fun invoke(): Flow<List<Note>>
}

class GetAllNotesUseCaseImpl(
    val repository: GetAllNotesRepo
) : GetAllNotesUseCase {

    override fun invoke(): Flow<List<Note>> = repository()

}