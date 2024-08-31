package com.dracul.feature_edit.ui.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dracul.common.aliases.CommonDrawables
import com.dracul.common.utills.getBlendedCardColor
import com.dracul.common.utills.getColor
import com.dracul.common.utills.noRippleClickable


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPickerDialog(currentColor: Int, onDismiss: () -> Unit, setColor: (Int) -> Unit) {
    val color = getBlendedCardColor(id = currentColor)
    val animatedColor = remember { Animatable(color) }

    LaunchedEffect(currentColor) {
        animatedColor.animateTo(color, animationSpec = tween(500, easing = EaseInOutCubic))
    }
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        Card(
            colors = CardDefaults.cardColors()
                .copy(containerColor = animatedColor.value),
            shape = RoundedCornerShape(32.dp)
        ) {
            FlowRow(modifier = Modifier.padding(8.dp), Arrangement.Center) {
                repeat(12) { colorId ->
                    val iconColor = getColor(id = colorId)
                    AnimatedContent(targetState = (currentColor == colorId), transitionSpec = {
                        scaleIn(
                            tween(durationMillis = 500, easing = EaseInCubic), initialScale = 0.4f
                        ) togetherWith scaleOut(
                            tween(durationMillis = 500, easing = EaseInCubic), targetScale = 0.4f
                        )
                    }, label = "") {
                        if (it) {
                            Image(painter = painterResource(id = CommonDrawables.ic_selected_circle),
                                colorFilter = ColorFilter.tint(iconColor),
                                contentDescription = "Color circle",
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .noRippleClickable { setColor(colorId) })
                        } else {
                            Image(painter = painterResource(id = CommonDrawables.ic_circle),
                                colorFilter = ColorFilter.tint(iconColor),
                                contentDescription = "Color circle",
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(32.dp))
                                    .noRippleClickable { setColor(colorId) })
                        }
                    }
                }
            }
        }
    }
}

