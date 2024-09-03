package com.dracul.images.domain.repository

interface DeleteImageByParentIdRepo {
    operator fun invoke(id: Long)
}