package com.dracul.task.domain.repository.sub_task

import com.dracul.task.domain.models.SubTask

interface DeleteSubTaskRepo {
    operator fun invoke(item: SubTask)
}