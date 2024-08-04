package com.dracul.notes.data.repository

import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.InsertNoteRepo
import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.notes.data.mapper.toEntity

class InsertNoteImpl:InsertNoteRepo {
    override fun invoke(item: Note) {
        com.dracul.database.db.DatabaseProviderWrap.noteDao.insert(item.toEntity())
    }
}