package com.dracul.task.domain.repository.task

interface UpdateWorkerByIdRepo {
    operator fun invoke(id: Long, workerId: String?, reminderTimestamp: Long?)
}