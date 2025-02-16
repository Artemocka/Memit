package com.dracul.task.data.repository.sub_task

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.task.data.mapper.toData
import com.dracul.task.domain.models.SubTask
import com.dracul.task.domain.repository.sub_task.UpdateSubTaskRepo

class UpdateSubTaskRepoImpl : UpdateSubTaskRepo {
    override fun invoke(image: SubTask) =
        DatabaseProviderWrap.subTaskDao.update(image.toData())
}