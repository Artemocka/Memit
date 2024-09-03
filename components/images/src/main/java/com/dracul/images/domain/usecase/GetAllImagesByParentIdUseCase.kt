package com.dracul.images.domain.usecase

interface GetAllImagesByParentIdUseCase {
    operator fun invoke(id: Long)
}

class GetAllImagesByParentIdImpl(
//    val repo:DeleteNoteByIdRepo
) : GetAllImagesByParentIdUseCase {
    override fun invoke(id: Long) {
//        repo(id)
    }
}