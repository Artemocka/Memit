package com.dracul.task.domain.repository.task

import com.dracul.task.domain.models.Task

interface GetTaskByIdRepo {
    operator fun invoke(id: Long): Task
}