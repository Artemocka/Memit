package com.dracul.task.domain.repository.sub_task

import com.dracul.task.domain.models.SubTask

interface UpdateSubTaskRepo {
    operator fun invoke(image: SubTask)
}