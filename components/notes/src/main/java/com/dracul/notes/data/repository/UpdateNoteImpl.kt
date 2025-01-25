package com.dracul.notes.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.notes.data.mapper.toEntity
import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.UpdateNoteRepo

class UpdateNoteImpl:UpdateNoteRepo {

    override fun invoke(item: Note) =
        DatabaseProviderWrap.noteDao.update(item.toEntity())

}