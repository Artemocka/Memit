package com.dracul.task.domain.usecase.task

import com.dracul.task.domain.repository.task.UpdateWorkerByIdRepo


interface UpdateWorkerByIdUseCase {
    operator fun invoke(id: Long, workerId: String?, reminderTimestamp: Long?)
}

class UpdateWorkerByIdUseCaseImpl(
    val repo: UpdateWorkerByIdRepo
) : UpdateWorkerByIdUseCase {

    override fun invoke(id: Long, workerId: String?, reminderTimestamp: Long?) =
        repo(id, workerId, reminderTimestamp)

}