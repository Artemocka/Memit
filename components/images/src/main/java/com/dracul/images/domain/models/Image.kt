package com.dracul.images.domain.models

import android.net.Uri
import androidx.compose.runtime.Immutable
@Immutable
data class Image(
    val id:Long,
    val parentId:Long,
    val uri: Uri,
)
