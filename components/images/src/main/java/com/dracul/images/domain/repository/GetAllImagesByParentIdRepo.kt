package com.dracul.images.domain.repository

import com.dracul.images.domain.models.Image
import kotlinx.coroutines.flow.Flow

interface GetAllImagesByParentIdRepo {
    operator fun invoke(id: Long): Flow<List<Image>>
}