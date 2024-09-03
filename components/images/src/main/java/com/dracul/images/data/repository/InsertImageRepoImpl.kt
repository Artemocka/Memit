package com.dracul.images.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.database.images.ImageEntity
import com.dracul.images.data.mapper.toEntity
import com.dracul.images.domain.models.Image
import com.dracul.images.domain.repository.InsertImageRepo

class InsertImageRepoImpl: InsertImageRepo {
    override fun invoke(image:Image) {
        DatabaseProviderWrap.imageDao.insert(image.toEntity())
    }
}