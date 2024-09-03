package com.dracul.images.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.database.images.ImageEntity
import com.dracul.images.data.mapper.toDomain
import com.dracul.images.data.mapper.toEntity
import com.dracul.images.domain.models.Image
import com.dracul.images.domain.repository.GetAllImagesByParentIdRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class GetAllImagesByParentIdRepoImpl: GetAllImagesByParentIdRepo {
    override fun invoke(id:Long): Flow<List<Image>> {
        return DatabaseProviderWrap.imageDao.getAllById(id).map { it -> it.map { it.toDomain() } }
    }
}