package com.dracul.task.domain.repository.task

import com.dracul.task.domain.models.Task


interface DeleteTaskRepo {
    operator fun invoke(item: Task)
}