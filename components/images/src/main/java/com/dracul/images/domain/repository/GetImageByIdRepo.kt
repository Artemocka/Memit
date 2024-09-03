package com.dracul.images.domain.repository

import com.dracul.images.domain.models.Image

interface GetImageByIdRepo {
    operator fun invoke(id: Long):Image
}