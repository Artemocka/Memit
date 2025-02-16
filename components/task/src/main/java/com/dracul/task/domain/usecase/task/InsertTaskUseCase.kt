package com.dracul.task.domain.usecase.task

import com.dracul.task.domain.models.Task
import com.dracul.task.domain.repository.task.InsertTaskRepo

interface InsertTaskUseCase {
    operator fun invoke(item: Task): Long
}

class InsertTaskUseCaseImpl(
    val repository: InsertTaskRepo
) : InsertTaskUseCase {

    override fun invoke(item: Task): Long = repository(item)

}