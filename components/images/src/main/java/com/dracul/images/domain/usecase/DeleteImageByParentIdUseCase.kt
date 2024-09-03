package com.dracul.images.domain.usecase

interface DeleteImageByParentIdUseCase {
    operator fun invoke(id:Long)
}
class DeleteImageByParentIdImpl(
//    val repo:DeleteNoteByIdRepo
):DeleteImageByParentIdUseCase{
    override fun invoke(id: Long) {
//        repo(id)
    }
}