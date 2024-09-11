package com.dracul.feature_edit.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.dracul.common.utills.noRippleClickable
import com.dracul.images.domain.models.Image

@Composable
fun ImageRow(
    modifier: Modifier = Modifier,
    images: List<Image>,
    onDelete: (imageId: Image) -> Unit,
    onClick: (index: Int) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val lazyListState = rememberLazyListState()

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .scrollable(scrollState, orientation = Orientation.Horizontal),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
        userScrollEnabled = true,
    ) {
        itemsIndexed(items = images, key = { _, item ->
            item.id
        }) { i, item ->
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context).data(item.uri).size(Size.ORIGINAL)
                    .memoryCacheKey(item.id.hashCode().toString())
                    .diskCacheKey(item.id.hashCode().toString())
                    .diskCachePolicy(CachePolicy.ENABLED).memoryCachePolicy(CachePolicy.ENABLED)
                    .build()
            )

            Box(
                modifier = Modifier
                    .animateItem(fadeInSpec = null, fadeOutSpec = null)
                    .then(
                        when (i) {
                            0 -> Modifier.padding(start = 16.dp)
                            images.lastIndex -> Modifier.padding(end = 16.dp)
                            else -> Modifier.padding()
                        }
                    )
                    .wrapContentSize(), contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .noRippleClickable {
                            onClick(i)
                        },
                    contentScale = ContentScale.Crop,
                )
                Image(
                    Icons.Filled.Cancel,
                    null,
                    alpha = 0.5f,
                    modifier = Modifier
                        .padding(2.dp)
                        .noRippleClickable {
                            onDelete(item)
                        },
                    colorFilter = ColorFilter.tint(
                        Color.White
                    )
                )
            }
        }
    }
}



