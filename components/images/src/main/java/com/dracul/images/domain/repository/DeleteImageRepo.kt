package com.dracul.images.domain.repository

import com.dracul.images.domain.models.Image

interface DeleteImageRepo {
    operator fun invoke(item: Image)
}