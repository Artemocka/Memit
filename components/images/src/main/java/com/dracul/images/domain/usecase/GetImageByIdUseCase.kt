package com.dracul.images.domain.usecase

import com.dracul.images.domain.repository.GetImageByIdRepo

interface GetImageByIdUseCase {
    operator fun invoke(id: Long)
}

class GetImageByIdImpl(
    val repo:GetImageByIdRepo
) : GetImageByIdUseCase {
    override fun invoke(id: Long) {
        repo(id)
    }
}