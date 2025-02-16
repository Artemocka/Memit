package com.dracul.task.domain.usecase.task

import com.dracul.task.domain.repository.task.UpdatePinnedTaskByIdRepo


interface UpdatePinnedTaskByIdUseCase {
    operator fun invoke(id: Long, pinned: Boolean)
}

class UpdatePinnedTaskByIdUseCaseImpl(val repository: UpdatePinnedTaskByIdRepo) : UpdatePinnedTaskByIdUseCase {

    override fun invoke(id: Long, pinned: Boolean) = repository(id, pinned)

}