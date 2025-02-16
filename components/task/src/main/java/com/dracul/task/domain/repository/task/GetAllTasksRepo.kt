package com.dracul.task.domain.repository.task

import com.dracul.task.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface GetAllTasksRepo {
    operator fun invoke(): Flow<List<Task>>
}