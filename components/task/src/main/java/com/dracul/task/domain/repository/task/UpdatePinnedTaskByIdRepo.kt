package com.dracul.task.domain.repository.task

interface UpdatePinnedTaskByIdRepo {
    operator fun invoke(id: Long, pinned: Boolean)
}