package com.dracul.images.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.images.data.mapper.toEntity
import com.dracul.images.domain.models.Image
import com.dracul.images.domain.repository.UpdateImageRepo

class UpdateImageRepoImpl : UpdateImageRepo {
    override fun invoke(image: Image) {
        DatabaseProviderWrap.imageDao.update(image.toEntity())
    }
}