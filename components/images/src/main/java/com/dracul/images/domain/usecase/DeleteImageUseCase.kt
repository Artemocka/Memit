package com.dracul.images.domain.usecase

import com.dracul.images.domain.models.Image
import com.dracul.images.domain.repository.DeleteImageRepo

interface DeleteImageUseCase {
    operator fun invoke(image: Image)
}

class DeleteImageImpl(
    val repository: DeleteImageRepo
) : DeleteImageUseCase {

    override fun invoke(image: Image) = repository(image)


}