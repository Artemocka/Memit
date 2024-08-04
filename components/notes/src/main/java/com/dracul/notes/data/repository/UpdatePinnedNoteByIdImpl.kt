package com.dracul.notes.data.repository

import com.dracul.notes.domain.repository.UpdatePinnedNoteByIdRepo
import com.dracul.database.db.DatabaseProviderWrap

class UpdatePinnedNoteByIdImpl : UpdatePinnedNoteByIdRepo {
    override fun invoke(id: Long, pinned: Boolean) {
        com.dracul.database.db.DatabaseProviderWrap.noteDao.updatePinnedById(id, pinned)
    }
}