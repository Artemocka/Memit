package com.dracul.task.domain.usecase.sub_task

import com.dracul.task.domain.models.SubTask
import com.dracul.task.domain.repository.sub_task.InsertSubTaskRepo

interface InsertSubTaskUseCase {
    operator fun invoke(image: SubTask): Long
}

class InsertSubTaskImpl(
    val repository: InsertSubTaskRepo
) : InsertSubTaskUseCase {

    override fun invoke(image: SubTask) = repository(image)

}