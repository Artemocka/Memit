package com.dracul.images.domain.usecase

import com.dracul.images.domain.models.Image
import com.dracul.images.domain.repository.GetImageByIdRepo

interface GetImageByIdUseCase {
    operator fun invoke(id: Long): Image
}

class GetImageByIdImpl(
    val repository: GetImageByIdRepo
) : GetImageByIdUseCase {

    override fun invoke(id: Long) = repository(id)

}