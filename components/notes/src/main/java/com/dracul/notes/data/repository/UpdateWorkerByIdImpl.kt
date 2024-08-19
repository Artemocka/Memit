package com.dracul.notes.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.notes.domain.repository.UpdateWorkerByIdRepo

class UpdateWorkerByIdImpl: UpdateWorkerByIdRepo {
    override fun invoke(id: Long, workerId: String?, reminderTimestamp: Long?) {
        DatabaseProviderWrap.noteDao.updateWorkerById(id, workerId, reminderTimestamp)
    }
}