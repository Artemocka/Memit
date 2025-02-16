package com.dracul.task.domain.repository.sub_task

import com.dracul.task.domain.models.SubTask
import kotlinx.coroutines.flow.Flow

interface GetAllSubTasksByParentIdRepo {
    operator fun invoke(id: Long): Flow<List<SubTask>>
}