package com.dracul.task.domain.usecase.sub_task

import com.dracul.task.domain.repository.sub_task.DeleteSubTaskByParentIdRepo

interface DeleteSubTaskByIdUseCase {
    operator fun invoke(id: Long)
}

class DeleteSubTaskByIdImpl(
    val repository: DeleteSubTaskByParentIdRepo
) : DeleteSubTaskByIdUseCase {

    override fun invoke(id: Long) = repository(id)

}