package com.dracul.task.domain.repository.task

interface DeleteTaskByIdRepo {
    operator fun invoke(id: Long)
}