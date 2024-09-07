package com.dracul.common.utills

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.colorResource
import com.dracul.common.aliases.CommonColors


@Composable
fun getColor(id: Int): Color {
    return when (id) {
        1 -> colorResource(CommonColors.color1)
        2 -> colorResource(CommonColors.color2)
        3 -> colorResource(CommonColors.color3)
        4 -> colorResource(CommonColors.color4)
        5 -> colorResource(CommonColors.color5)
        6 -> colorResource(CommonColors.color6)
        7 -> colorResource(CommonColors.color7)
        8 -> colorResource(CommonColors.color8)
        9 -> colorResource(CommonColors.color9)
        10 -> colorResource(CommonColors.color10)
        11 -> colorResource(CommonColors.color11)
        else -> MaterialTheme.colorScheme.secondaryContainer
    }
}



@Composable
fun getBlendedColor(id: Int): Color {
    val color = getColor(id = id)
    val white = colorResource(id = CommonColors.white)
    return lerp(color, white, 0.35f)
}

@Composable
fun getBlendedColor(color: Color): Color {
    val white = colorResource(id = CommonColors.black)
    return lerp(color, white, 0.1f)
}

@Composable
fun getBlendedCardColor(id: Int): Color {
    val color = getColor(id = id)
    val black = colorResource(id = CommonColors.black)
    return lerp(color, black, 0.1f)
}