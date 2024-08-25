package com.dracul.notes.data.repository

import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.GetNoteByIdRepo
import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.notes.data.mapper.toDomain

class GetNoteByIdImpl: GetNoteByIdRepo {
    override fun invoke(id: Long): Note {
        return com.dracul.database.db.DatabaseProviderWrap.noteDao.getById(id).toDomain()
    }
}