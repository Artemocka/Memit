package com.dracul.images.domain.models

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable

@Immutable
data class Image(
    val id:Long,
    val parentId:Long,
    val bitmap: Bitmap,
)
