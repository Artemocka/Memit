package com.dracul.images.domain.usecase

import com.dracul.images.domain.models.Image
import com.dracul.images.domain.repository.InsertImageRepo

interface InsertImageUseCase {
    operator fun invoke(image: Image)
}

class InsertImageImpl(
    val repository: InsertImageRepo
) : InsertImageUseCase {

    override fun invoke(image: Image) = repository(image)

}