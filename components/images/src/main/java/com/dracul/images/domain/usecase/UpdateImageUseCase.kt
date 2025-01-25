package com.dracul.images.domain.usecase

import com.dracul.images.domain.models.Image
import com.dracul.images.domain.repository.UpdateImageRepo

interface UpdateImageUseCase {
    operator fun invoke(image: Image)
}

class UpdateImageImpl(
    val repository: UpdateImageRepo
) : UpdateImageUseCase {
    override fun invoke(image: Image) = repository(image)

}