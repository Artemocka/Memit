package com.dracul.images.domain.repository

import com.dracul.database.images.ImageEntity
import com.dracul.images.domain.models.Image

interface DeleteImageByParentIdRepo {
    operator fun invoke(id: Long)
}