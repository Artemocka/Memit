package com.dracul.feature_main.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.dracul.common.utills.noRippleClickable
import com.dracul.images.domain.models.Image

@NonRestartableComposable
@Composable
fun AsyncImage(
    noteId: Long,
    images: List<Image>,
    imageIndex: Int,
    onImageClick: (Long, Int) -> Unit,
) {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(images[imageIndex].uri).size(Size.ORIGINAL)
            .memoryCacheKey(images[imageIndex].id.hashCode().toString())
            .diskCacheKey(images[imageIndex].id.hashCode().toString())
            .diskCachePolicy(CachePolicy.ENABLED).memoryCachePolicy(CachePolicy.ENABLED).build()
    )
    Image(
        modifier = Modifier
            .then(
                when (imageIndex) {
                    0 -> Modifier.padding(start = 6.dp)
                    images.lastIndex -> Modifier.padding(end = 6.dp)
                    else -> Modifier.padding()
                }
            )
            .padding(horizontal = 3.dp)
            .size(48.dp, 48.dp)
            .clip(RoundedCornerShape(8.dp))
            .noRippleClickable {
                onImageClick(noteId, imageIndex)
            },
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
}