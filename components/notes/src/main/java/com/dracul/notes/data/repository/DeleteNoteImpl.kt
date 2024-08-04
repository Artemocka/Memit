package com.dracul.notes.data.repository

import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.DeleteNoteRepo
import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.notes.data.mapper.toEntity

class DeleteNoteImpl : DeleteNoteRepo {
    override fun invoke(item: Note) {
        com.dracul.database.db.DatabaseProviderWrap.noteDao.delete(item.toEntity())
    }
}