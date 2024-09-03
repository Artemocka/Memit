package com.dracul.images.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.images.data.mapper.toDomain
import com.dracul.images.data.mapper.toEntity
import com.dracul.images.domain.models.Image
import com.dracul.images.domain.repository.GetAllImagesByParentIdRepo
import com.dracul.images.domain.repository.GetImageByIdRepo

class GetImagesByIdRepoImpl: GetImageByIdRepo {
    override fun invoke(id:Long): Image {
        return DatabaseProviderWrap.imageDao.getById(id).toDomain()
    }
}