package com.dracul.notes.domain.usecase

import com.dracul.notes.domain.repository.UpdateWorkerByIdRepo

interface UpdateWorkerByIdUseCase {
    operator fun invoke(id: Long, workerId: String?, reminderTimestamp: Long?)
}


class UpdateWorkerByIdUseCaseImpl(
    val repo: UpdateWorkerByIdRepo
) : UpdateWorkerByIdUseCase {
    override fun invoke(id: Long, workerId: String?, reminderTimestamp: Long?) {
        repo(id, workerId, reminderTimestamp)
    }

}