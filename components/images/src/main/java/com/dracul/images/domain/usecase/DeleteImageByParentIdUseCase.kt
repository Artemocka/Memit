package com.dracul.images.domain.usecase

import com.dracul.images.domain.repository.DeleteImageByParentIdRepo

interface DeleteImageByParentIdUseCase {
    operator fun invoke(id:Long)
}
class DeleteImageByParentIdImpl(
    val repo:DeleteImageByParentIdRepo
):DeleteImageByParentIdUseCase{
    override fun invoke(id: Long) {
        repo(id)
    }
}