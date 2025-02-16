package com.dracul.task.data.repository.sub_task

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.task.data.mapper.toData
import com.dracul.task.domain.models.SubTask
import com.dracul.task.domain.repository.sub_task.DeleteSubTaskRepo

class DeleteSubTaskRepoImpl() : DeleteSubTaskRepo {
    override fun invoke(item: SubTask) =
        DatabaseProviderWrap.subTaskDao.delete(item.toData())
}