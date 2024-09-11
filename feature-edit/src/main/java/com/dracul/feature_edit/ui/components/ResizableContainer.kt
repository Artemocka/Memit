package com.dracul.feature_edit.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dracul.common.utills.getBlendedColor
import kotlin.math.abs

@Composable
fun ResizableContainer(color: Color, content: @Composable () -> Unit) {
    var targetHeight by remember { mutableStateOf(140.dp) }
    val animatedHeight by animateDpAsState(
        targetValue = targetHeight, animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow
        ), label = ""
    )
    Box(
        Modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .background(getBlendedColor(color), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectDragGestures(onDrag = { change, dragAmount ->
                    change.consume()
                    val (x, y) = dragAmount
                    if (abs(x) < abs(y)) {
                        when {
                            y > 0 -> {
                                targetHeight = 32.dp

                            }

                            y < 0 -> {
                                targetHeight = 140.dp
                            }
                        }
                    }
                })
            }) {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .padding(top = 12.dp)
                    .size(32.dp, 6.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
            )
            Box(Modifier.padding(top = 16.dp)) {
                content()
            }
        }
    }
}


@Preview
@Composable
fun ResizableContainer() {
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
    ) {
        ResizableContainer(MaterialTheme.colorScheme.secondaryContainer) {}
    }
}
