package com.dracul.task.data.mapper

import com.dracul.database.sub_tasks.SubTaskEntity
import com.dracul.task.domain.models.SubTask

internal fun SubTaskEntity.toDomain(): SubTask =
    SubTask(
        id = id,
        parentId = parentId,
        name = name,
        isDone = isDone,
    )

internal fun SubTask.toData(): SubTaskEntity =
    SubTaskEntity(
        id = id,
        parentId = parentId,
        name = name,
        isDone = isDone,
    )