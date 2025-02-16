package com.dracul.task.domain.repository.sub_task

interface DeleteSubTaskByParentIdRepo {
    operator fun invoke(id: Long)
}