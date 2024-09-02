package com.dracul.feature_edit.ui.components

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ImageRow(modifier: Modifier = Modifier, images: List<Bitmap>) {

    val scrollState = rememberScrollState()
    Row(
        modifier = modifier.scrollable(scrollState, orientation = Orientation.Horizontal)
    ) {
        repeat(images.size) {
            Image(
                bitmap = images[it].asImageBitmap(), null
            )
        }
    }
}


@Preview
@Composable
private fun ImageRowPreview() {


    val bitmap = Bitmap.createBitmap(400,400, Bitmap.Config.ALPHA_8)
    bitmap.setPixel(20, 20, Color.RED)
    bitmap.setPixel(70, 50, Color.RED)
    bitmap.setPixel(30, 80, Color.RED)
    ImageRow(modifier = Modifier,listOf(bitmap, bitmap))
}

