package com.dracul.task.domain.usecase.sub_task

import com.dracul.task.domain.models.SubTask
import com.dracul.task.domain.repository.sub_task.GetSubTaskByIdRepo

interface GetSubTaskByIdUseCase {
    operator fun invoke(id: Long): SubTask
}

class GetSubTaskByIdImpl(
    val repository: GetSubTaskByIdRepo
) : GetSubTaskByIdUseCase {

    override fun invoke(id: Long) = repository(id)

}