package com.dracul.images.domain.usecase

import com.dracul.images.domain.repository.GetAllImagesByParentIdRepo

interface GetAllImagesByParentIdUseCase {
    operator fun invoke(id: Long)
}

class GetAllImagesByParentIdImpl(
    val repo:GetAllImagesByParentIdRepo
) : GetAllImagesByParentIdUseCase {
    override fun invoke(id: Long) {
        repo(id)
    }
}