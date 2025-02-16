package com.dracul.task.data.mapper

import com.dracul.database.tasks.TaskEntity
import com.dracul.task.domain.models.Task

internal fun TaskEntity.toDomain(): Task =
    Task(
        id = id,
        title = title,
        color = color,
        pinned = pinned,
        workerId = workerId,
        reminderTimeStamp = reminderTimeStamp,
    )

internal fun Task.toData(): TaskEntity =
    TaskEntity(
        id = id,
        title = title,
        color = color,
        pinned = pinned,
        workerId = workerId,
        reminderTimeStamp = reminderTimeStamp,
    )