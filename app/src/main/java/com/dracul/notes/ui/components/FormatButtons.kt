package com.dracul.notes.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dracul.notes.R
import com.dracul.notes.navigation.EditNoteComponent
import com.dracul.notes.navigation.events.EditNoteEvent
import com.dracul.notes.ui.screens.getBlendedColor
import com.dracul.notes.ui.screens.getColor
import com.mohamedrejeb.richeditor.model.RichTextState


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormatButtons(
    isFocused: Boolean,
    content: RichTextState,
    component: EditNoteComponent,
    color: Int
) {

    val underlineExpr = content.currentSpanStyle.textDecoration == TextDecoration.Underline ||
            content.currentSpanStyle.textDecoration?.contains(
                TextDecoration.combine(
                    listOf(TextDecoration.Underline, TextDecoration.LineThrough)
                )
            ) == true
    val lineThroughExpr = content.currentSpanStyle.textDecoration == TextDecoration.LineThrough ||
            content.currentSpanStyle.textDecoration?.contains(
                TextDecoration.combine(
                    listOf(TextDecoration.Underline, TextDecoration.LineThrough)
                )
            ) == true

    AnimatedVisibility(
        visible = isFocused, enter = slideInVertically(

            initialOffsetY = { +it },
            animationSpec = tween(
                200,
                delayMillis = if (!WindowInsets.isImeVisible) 450 else 0
            )
        ) + fadeIn(tween(200)), exit = slideOutVertically(
            targetOffsetY = { +it },
            animationSpec = tween(
                200,
                delayMillis = if (!WindowInsets.isImeVisible) 450 else 0
            )
        ) + fadeOut(tween(200))
    ) {
        Row(
            modifier = Modifier
                .imePadding()
                .padding(horizontal = 2.dp)
        ) {
            IconButton(
                painter = painterResource(id = R.drawable.ic_bold),
                expr = content.currentSpanStyle.fontWeight == FontWeight.Bold,
                color = color,
            ) {
                component.onEvent(EditNoteEvent.SetBold)
            }
            IconButton(
                painter = painterResource(id = R.drawable.ic_italic),
                expr = content.currentSpanStyle.fontStyle == FontStyle.Italic,
                color = color,
            ) {
                component.onEvent(EditNoteEvent.SetItalic)
            }
            IconButton(
                painter = painterResource(id = R.drawable.ic_linethrough),
                expr = lineThroughExpr,
                color = color,
            ) {
                component.onEvent(EditNoteEvent.SetLinethrough)
            }
            IconButton(
                painter = painterResource(id = R.drawable.ic_underline),
                expr = underlineExpr,
                color = color,
            ) {
                component.onEvent(EditNoteEvent.SetUnderline)
            }


            IconButton(
                painter = painterResource(id = R.drawable.ic_align_left),
                expr = content.currentParagraphStyle.textAlign == TextAlign.Start,
                color = color,
            ) {
                component.onEvent(EditNoteEvent.SetAlignStart)
            }

            IconButton(
                painter = painterResource(id = R.drawable.ic_align_center),
                expr = content.currentParagraphStyle.textAlign == TextAlign.Center,
                color = color,
            ) {
                component.onEvent(EditNoteEvent.SetAlignCenter)
            }

            IconButton(
                painter = painterResource(id = R.drawable.ic_align_right),
                expr = content.currentParagraphStyle.textAlign == TextAlign.End,
                color = color,
            ) {
                component.onEvent(EditNoteEvent.SetAlignEnd)
            }

            IconButton(imageVector = Icons.Filled.Clear, color = color) {
                component.onEvent(EditNoteEvent.ClearALl)
            }

        }
    }
}


@Composable
fun IconButton(
    painter: Painter,
    expr: Boolean,
    color: Int,
    onClick: (() -> Unit),
) {
    OutlinedIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors().copy(
            containerColor = if (expr) getBlendedColor(
                id = color
            ) else getColor(id = color),
            contentColor = if (expr) getColor(
                id = color
            ) else MaterialTheme.colorScheme.onSurface,
        ),
        shape = RoundedCornerShape(32),
        border = BorderStroke(0.dp, color = Color.Transparent),
    ) {
        Icon(
            painter = painter, contentDescription = null
        )
    }
}

@Composable
fun IconButton(
    imageVector: ImageVector,
    color: Int,
    onClick: (() -> Unit),
) {
    OutlinedIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors().copy(
            containerColor = getColor(id = color),
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        shape = RoundedCornerShape(32),
        border = BorderStroke(0.dp, color = Color.Transparent),
    ) {
        Icon(
            imageVector = imageVector, contentDescription = null
        )
    }
}
