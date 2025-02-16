package com.dracul.task.domain.repository.sub_task

import com.dracul.task.domain.models.SubTask

interface GetSubTaskByIdRepo {
    operator fun invoke(id: Long): SubTask
}