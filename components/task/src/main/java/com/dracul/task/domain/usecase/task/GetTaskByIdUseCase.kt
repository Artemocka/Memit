package com.dracul.task.domain.usecase.task

import com.dracul.task.domain.models.Task
import com.dracul.task.domain.repository.task.GetTaskByIdRepo

interface GetTaskByIdUseCase {
    operator fun invoke(id: Long): Task
}

class GetTaskByIdUseCaseImpl(
    val repository: GetTaskByIdRepo
) : GetTaskByIdUseCase {

    override fun invoke(id: Long): Task = repository(id)

}