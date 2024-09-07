package com.dracul.images.data.mapper

import com.dracul.database.images.ImageEntity
import com.dracul.images.domain.models.Image


internal fun ImageEntity.toDomain(): Image = Image(
    id = id, parentId = parentId, uri = uri
)

internal fun Image.toEntity(): ImageEntity = ImageEntity(
    id = id, parentId = parentId,  uri = uri
)