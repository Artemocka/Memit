package com.dracul.task.data.repository.sub_task

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.task.data.mapper.toDomain
import com.dracul.task.domain.models.SubTask
import com.dracul.task.domain.repository.sub_task.GetSubTaskByIdRepo

class GetSubTaskByIdRepoImpl : GetSubTaskByIdRepo {
    override fun invoke(id: Long): SubTask =
        DatabaseProviderWrap.subTaskDao.getById(id).toDomain()
}