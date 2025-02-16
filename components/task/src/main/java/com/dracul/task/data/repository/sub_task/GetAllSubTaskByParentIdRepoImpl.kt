package com.dracul.task.data.repository.sub_task

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.task.data.mapper.toDomain
import com.dracul.task.domain.models.SubTask
import com.dracul.task.domain.repository.sub_task.GetAllSubTasksByParentIdRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllSubTaskByParentIdRepoImpl : GetAllSubTasksByParentIdRepo {
    override fun invoke(id: Long): Flow<List<SubTask>> =
        DatabaseProviderWrap.subTaskDao.getAllById(id).map { it -> it.map { it.toDomain() } }
}