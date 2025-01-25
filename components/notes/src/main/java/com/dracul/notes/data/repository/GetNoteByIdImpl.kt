package com.dracul.notes.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.notes.data.mapper.toDomain
import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.GetNoteByIdRepo

class GetNoteByIdImpl : GetNoteByIdRepo {

    override fun invoke(id: Long): Note =
        DatabaseProviderWrap.noteDao.getById(id).toDomain()

}