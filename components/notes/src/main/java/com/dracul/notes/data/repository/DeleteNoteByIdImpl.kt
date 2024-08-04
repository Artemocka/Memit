package com.dracul.notes.data.repository

import com.dracul.notes.domain.repository.DeleteNoteByIdRepo
import com.dracul.database.db.DatabaseProviderWrap

class DeleteNoteByIdImpl:DeleteNoteByIdRepo {
    override fun invoke(id: Long) {
        com.dracul.database.db.DatabaseProviderWrap.noteDao.deleteById(id)
    }
}