package com.dracul.feature_edit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.dracul.images.domain.models.Image

@Composable
fun ImageRow(modifier: Modifier = Modifier, images: List<Image>) {
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
        items(images.size) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(images[it].uri)
                    .size(Size.ORIGINAL)
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .then(
                        when (it) {
                            0 -> Modifier.padding(start = 16.dp)
                            images.lastIndex -> Modifier.padding(end = 16.dp)
                            else -> Modifier.padding()
                        }
                    )
                    .size(88.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )
        }
    }
}



