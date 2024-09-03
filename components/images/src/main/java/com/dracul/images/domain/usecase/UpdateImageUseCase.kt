package com.dracul.images.domain.usecase

import com.dracul.images.domain.models.Image

interface UpdateImageUseCase {
    operator fun invoke(image: Image)
}

class UpdateImageImpl(
//    val repo:DeleteNoteByIdRepo
) : UpdateImageUseCase {
    override fun invoke(image: Image) {
//        repo(id)
    }
}