package com.dracul.images.domain.usecase

import com.dracul.images.domain.models.Image

interface InsertImageUseCase {
    operator fun invoke(image: Image)
}

class InsertImageImpl(
//    val repo:DeleteNoteByIdRepo
) : InsertImageUseCase {
    override fun invoke(image: Image) {
//        repo(id)
    }
}