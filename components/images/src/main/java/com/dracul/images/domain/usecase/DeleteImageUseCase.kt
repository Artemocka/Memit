package com.dracul.images.domain.usecase

import com.dracul.images.domain.models.Image

interface DeleteImageUseCase {
    operator fun invoke(image: Image)
}

class DeleteImageImpl(
//    val repo:DeleteNoteByIdRepo
) : DeleteImageUseCase {
    override fun invoke(image: Image) {
//        repo(id)
    }
}