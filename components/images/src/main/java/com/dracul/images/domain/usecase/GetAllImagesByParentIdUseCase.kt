package com.dracul.images.domain.usecase

import com.dracul.images.domain.models.Image
import com.dracul.images.domain.repository.GetAllImagesByParentIdRepo
import kotlinx.coroutines.flow.Flow

interface GetAllImagesByParentIdUseCase {
    operator fun invoke(id: Long): Flow<List<Image>>
}

class GetAllImagesByParentIdImpl(
    val repo:GetAllImagesByParentIdRepo
) : GetAllImagesByParentIdUseCase {
    override fun invoke(id: Long): Flow<List<Image>> {
        return repo(id)
    }
}