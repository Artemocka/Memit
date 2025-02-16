package com.dracul.notes.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.notes.data.mapper.toData
import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.repository.InsertNoteRepo

class InsertNoteImpl : InsertNoteRepo {

    override fun invoke(item: Note): Long =
        DatabaseProviderWrap.noteDao.insert(item.toData())

}