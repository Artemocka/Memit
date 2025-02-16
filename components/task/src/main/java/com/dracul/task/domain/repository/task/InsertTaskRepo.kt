package com.dracul.task.domain.repository.task

import com.dracul.task.domain.models.Task

interface InsertTaskRepo {
    operator fun invoke(item: Task): Long
}