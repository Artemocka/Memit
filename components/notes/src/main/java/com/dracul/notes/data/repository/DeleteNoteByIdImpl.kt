package com.dracul.notes.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.notes.domain.repository.DeleteNoteByIdRepo

class DeleteNoteByIdImpl:DeleteNoteByIdRepo {
    override fun invoke(id: Long) {
        DatabaseProviderWrap.noteDao.deleteById(id)
    }
}