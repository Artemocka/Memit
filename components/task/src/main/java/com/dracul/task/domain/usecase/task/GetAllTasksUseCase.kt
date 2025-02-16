package com.dracul.task.domain.usecase.task

import com.dracul.task.domain.models.Task
import com.dracul.task.domain.repository.task.GetAllTasksRepo
import kotlinx.coroutines.flow.Flow

interface GetAllTasksUseCase {
    operator fun invoke(): Flow<List<Task>>
}

class GetAllTasksUseCaseImpl(
    val repository: GetAllTasksRepo
) : GetAllTasksUseCase {

    override fun invoke(): Flow<List<Task>> = repository()

}