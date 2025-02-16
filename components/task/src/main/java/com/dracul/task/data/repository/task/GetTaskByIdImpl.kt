package com.dracul.task.data.repository.task

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.task.data.mapper.toDomain
import com.dracul.task.domain.models.Task
import com.dracul.task.domain.repository.task.GetTaskByIdRepo

class GetTaskByIdImpl : GetTaskByIdRepo {

    override fun invoke(id: Long): Task =
        DatabaseProviderWrap.taskDao.getById(id).toDomain()

}