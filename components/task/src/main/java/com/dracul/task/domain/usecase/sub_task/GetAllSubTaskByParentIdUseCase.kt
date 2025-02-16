package com.dracul.task.domain.usecase.sub_task

import com.dracul.task.domain.models.SubTask
import com.dracul.task.domain.repository.sub_task.GetAllSubTasksByParentIdRepo
import kotlinx.coroutines.flow.Flow

interface GetAllSubTaskByParentIdUseCase {
    operator fun invoke(id: Long): Flow<List<SubTask>>
}

class GetAllSubTaskByParentIdImpl(
    val repository: GetAllSubTasksByParentIdRepo
) : GetAllSubTaskByParentIdUseCase {

    override fun invoke(id: Long): Flow<List<SubTask>> = repository(id)

}