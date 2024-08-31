package com.dracul.notes.data.repository

import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.GetAllNotesRepo
import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.notes.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllNotesImpl:GetAllNotesRepo {
    override fun invoke(): Flow<List<Note>> {
        return DatabaseProviderWrap.noteDao.getAll().map { it -> it.map { it.toDomain() } }
    }
}