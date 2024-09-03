package com.dracul.images.data.repository

import com.dracul.database.db.DatabaseProviderWrap
import com.dracul.images.data.mapper.toEntity
import com.dracul.images.domain.models.Image
import com.dracul.images.domain.repository.DeleteImageRepo

class DeleteImageRepoImpl():DeleteImageRepo{
    override fun invoke(item: Image) {
        DatabaseProviderWrap.imageDao.delete(item.toEntity())
    }
}