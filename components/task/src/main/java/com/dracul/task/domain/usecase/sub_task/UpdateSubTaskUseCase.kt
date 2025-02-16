package com.dracul.task.domain.usecase.sub_task

import com.dracul.task.domain.models.SubTask
import com.dracul.task.domain.repository.sub_task.UpdateSubTaskRepo

interface UpdateSubTaskUseCase {
    operator fun invoke(image: SubTask)
}

class UpdateSubTaskImpl(
    val repository: UpdateSubTaskRepo
) : UpdateSubTaskUseCase {

    override fun invoke(image: SubTask) = repository(image)

}