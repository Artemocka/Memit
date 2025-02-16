package com.dracul.task.domain.usecase.task

import com.dracul.task.domain.models.Task
import com.dracul.task.domain.repository.task.UpdateTaskRepo


interface UpdateTaskUseCase {
    operator fun invoke(item: Task)
}

class UpdateTaskUseCaseImpl(
    val repository: UpdateTaskRepo
) : UpdateTaskUseCase {

    override fun invoke(item: Task) = repository(item)

}