package com.dracul.task.domain.repository.task

import com.dracul.task.domain.models.Task

interface UpdateTaskRepo {
    operator fun invoke(item: Task)
}