package com.dracul.notes.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.notes.domain.repository.UpdatePinnedNoteByIdRepo

class UpdatePinnedNoteByIdImpl : UpdatePinnedNoteByIdRepo {

    override fun invoke(id: Long, pinned: Boolean) =
        DatabaseProviderWrap.noteDao.updatePinnedById(id, pinned)

}