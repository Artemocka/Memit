package com.dracul.task.domain.repository.sub_task

import com.dracul.task.domain.models.SubTask

interface InsertSubTaskRepo {
    operator fun invoke(image: SubTask): Long
}