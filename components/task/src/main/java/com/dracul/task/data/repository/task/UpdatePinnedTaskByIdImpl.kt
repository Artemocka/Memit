package com.dracul.task.data.repository.task

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.task.domain.repository.task.UpdatePinnedTaskByIdRepo

class UpdatePinnedTaskByIdImpl : UpdatePinnedTaskByIdRepo {

    override fun invoke(id: Long, pinned: Boolean) =
        DatabaseProviderWrap.taskDao.updatePinnedById(id, pinned)

}