package com.dracul.task.domain.usecase.task

import com.dracul.task.domain.models.Task
import com.dracul.task.domain.repository.task.DeleteTaskRepo

interface DeleteTaskUseCase {
    operator fun invoke(item: Task)
}

class DeleteTaskUseCaseImpl(
    val repository: DeleteTaskRepo
) : DeleteTaskUseCase {

    override fun invoke(item: Task) = repository(item)

}