package com.dracul.task.data.repository.task

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.task.data.mapper.toDomain
import com.dracul.task.domain.models.Task
import com.dracul.task.domain.repository.task.GetAllTasksRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllTasksImpl : GetAllTasksRepo {

    override fun invoke(): Flow<List<Task>> =
        DatabaseProviderWrap.taskDao.getAll().map { it -> it.map { it.toDomain() } }

}