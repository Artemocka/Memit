package com.dracul.images.domain.usecase

interface GetImageByIdUseCase {
    operator fun invoke(id: Long)
}

class GetImageByIdImpl(
//    val repo:DeleteNoteByIdRepo
) : GetImageByIdUseCase {
    override fun invoke(id: Long) {
//        repo(id)
    }
}