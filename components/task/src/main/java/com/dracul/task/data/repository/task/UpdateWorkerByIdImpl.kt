package com.dracul.task.data.repository.task

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.task.domain.repository.task.UpdateWorkerByIdRepo

class UpdateWorkerByIdImpl : UpdateWorkerByIdRepo {

    override fun invoke(id: Long, workerId: String?, reminderTimestamp: Long?) =
        DatabaseProviderWrap.noteDao.updateWorkerById(id, workerId, reminderTimestamp)

}