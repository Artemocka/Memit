package com.dracul.task.domain.usecase.task

import com.dracul.task.domain.repository.task.DeleteTaskByIdRepo


interface DeleteTaskByIdUseCase {
    operator fun invoke(id: Long)
}

class DeleteTaskByIdUseCaseImpl(
    val repository: DeleteTaskByIdRepo
) : DeleteTaskByIdUseCase {

    override fun invoke(id: Long) = repository(id)

}