package com.dracul.images.domain.usecase

import com.dracul.images.domain.repository.DeleteImageByParentIdRepo

interface DeleteImageByIdUseCase {
    operator fun invoke(id:Long)
}
class DeleteImageByParentIdImpl(
    val repo:DeleteImageByParentIdRepo
):DeleteImageByIdUseCase{
    override fun invoke(id: Long) {
        repo(id)
    }
}