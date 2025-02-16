package com.dracul.task.domain.usecase.sub_task

import com.dracul.task.domain.models.SubTask
import com.dracul.task.domain.repository.sub_task.DeleteSubTaskRepo

interface DeleteSubTaskUseCase {
    operator fun invoke(image: SubTask)
}

class DeleteSubTaskImpl(
    val repository: DeleteSubTaskRepo
) : DeleteSubTaskUseCase {

    override fun invoke(image: SubTask) = repository(image)


}