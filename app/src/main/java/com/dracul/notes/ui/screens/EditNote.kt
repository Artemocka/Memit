package com.dracul.notes.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dracul.notes.R
import com.dracul.notes.navigation.EditNoteComponent
import com.dracul.notes.navigation.events.EditNoteEvent
import com.dracul.notes.navigation.events.EditNoteEvent.Back
import com.dracul.notes.navigation.events.EditNoteEvent.UpdateTitle
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditNoteScreen(
    component: EditNoteComponent
) {
    val title = component.title
    val content = component.content
    val color by component.color

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Note") },
                navigationIcon = {
                    IconButton({ component.onEvent(Back) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = getColor(id = color))
            )
        }, modifier = Modifier.background(color = getColor(id = color))
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .background(color = getColor(id = color))
                .navigationBarsPadding(),
        ) {
            OutlinedTextField(
                placeholder = { Text(text = "Title (optional)") },
                value = title.value,
                onValueChange = { component.onEvent(UpdateTitle(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,

                    ),
                textStyle = TextStyle(fontSize = 20.sp)
            )
            HorizontalDivider(
                Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurface
            )
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()
            RichTextEditor(
                state = content,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(Color.Transparent),
                interactionSource = interactionSource,
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                colors = RichTextEditorDefaults.richTextEditorColors(
                    containerColor = getColor(id = color),
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    selectionColors = TextSelectionColors(
                        getBlendedColor(id = color), getBlendedColor(id = color)
                    ),
                ),
                placeholder = { Text(text = "Content") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Default,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
            )



            AnimatedVisibility(
                visible = isFocused, enter = slideInVertically(

                    initialOffsetY = { +it },
                    animationSpec = tween(
                        200,
                        delayMillis = if (!WindowInsets.isImeVisible) 400 else 0
                    )
                ) + fadeIn(tween(200)), exit = slideOutVertically(
                    targetOffsetY = { +it },
                    animationSpec = tween(
                        200,
                        delayMillis = if (!WindowInsets.isImeVisible) 400 else 0
                    )
                ) + fadeOut(tween(200))
            ) {
                Row(
                    modifier = Modifier
                        .imePadding()
                        .padding(horizontal = 2.dp)
                ) {
                    OutlinedIconButton(
                        onClick = { component.onEvent(EditNoteEvent.SetBold) },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = if (content.currentSpanStyle.fontWeight == FontWeight.Bold) getBlendedColor(
                                id = color
                            ) else getColor(id = color),
                            contentColor = if (content.currentSpanStyle.fontWeight == FontWeight.Bold) getColor(
                                id = color
                            )
                            else MaterialTheme.colorScheme.onBackground,
                        ),
                        shape = RoundedCornerShape(32),
                        border = BorderStroke(0.dp, color = Color.Transparent),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bold),
                            contentDescription = "Bold"
                        )
                    }
                    OutlinedIconButton(
                        modifier = Modifier.padding(0.dp),
                        onClick = { component.onEvent(EditNoteEvent.SetItalic) },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = if (content.currentSpanStyle.fontStyle == FontStyle.Italic) getBlendedColor(
                                id = color
                            ) else getColor(id = color),
                            contentColor = if (content.currentSpanStyle.fontStyle == FontStyle.Italic) getColor(
                                id = color
                            ) else MaterialTheme.colorScheme.onSurface,
                        ),
                        shape = RoundedCornerShape(32),
                        border = BorderStroke(0.dp, color = Color.Transparent),

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_italic),
                            contentDescription = "Italic".trim()
                        )
                    }
                    OutlinedIconButton(
                        onClick = { component.onEvent(EditNoteEvent.SetStrokethrough) },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = if (content.currentSpanStyle.textDecoration == TextDecoration.LineThrough) getBlendedColor(
                                id = color
                            ) else getColor(id = color),
                            contentColor = if (content.currentSpanStyle.textDecoration == TextDecoration.LineThrough) getColor(
                                id = color
                            ) else MaterialTheme.colorScheme.onSurface,
                        ),
                        shape = RoundedCornerShape(32),
                        border = BorderStroke(0.dp, color = Color.Transparent),

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_strikethrough),
                            contentDescription = "Strokethrough"
                        )
                    }
                    OutlinedIconButton(
                        onClick = { component.onEvent(EditNoteEvent.SetAlignStart) },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = if (content.currentParagraphStyle.textAlign == TextAlign.Start) getBlendedColor(
                                id = color
                            ) else getColor(id = color),
                            contentColor = if (content.currentParagraphStyle.textAlign == TextAlign.Start) getColor(
                                id = color
                            ) else MaterialTheme.colorScheme.onSurface,
                        ),
                        shape = RoundedCornerShape(32),
                        border = BorderStroke(0.dp, color = Color.Transparent),

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_align_left),
                            contentDescription = "Align center"
                        )
                    }
                    OutlinedIconButton(
                        onClick = { component.onEvent(EditNoteEvent.SetAlignCenter) },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = if (content.currentParagraphStyle.textAlign == TextAlign.Center) getBlendedColor(
                                id = color
                            ) else getColor(id = color),
                            contentColor = if (content.currentParagraphStyle.textAlign == TextAlign.Center) getColor(
                                id = color
                            ) else MaterialTheme.colorScheme.onSurface,
                        ),
                        shape = RoundedCornerShape(32),
                        border = BorderStroke(0.dp, color = Color.Transparent),

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_align_center),
                            contentDescription = "Align center"
                        )
                    }
                    OutlinedIconButton(
                        onClick = { component.onEvent(EditNoteEvent.SetAlignEnd) },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = if (content.currentParagraphStyle.textAlign == TextAlign.End) getBlendedColor(
                                id = color
                            ) else getColor(id = color),
                            contentColor = if (content.currentParagraphStyle.textAlign == TextAlign.End) getColor(
                                id = color
                            ) else MaterialTheme.colorScheme.onSurface,
                        ),
                        shape = RoundedCornerShape(32),
                        border = BorderStroke(0.dp, color = Color.Transparent),

                        ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_align_right),
                            contentDescription = "Align center"
                        )
                    }

                    OutlinedIconButton(
                        onClick = { component.onEvent(EditNoteEvent.ClearALl) },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = getColor(id = color),
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        shape = RoundedCornerShape(32),
                        border = BorderStroke(0.dp, color = Color.Transparent),

                        ) {
                        Icon(
                            imageVector = Icons.Filled.Clear, contentDescription = "Clear format"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RectangleIconButton(
    contentColor: Color,
    containerColor: Color,
    @DrawableRes id: Int,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .wrapContentSize()
            .padding(0.dp),
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(0),

        ) {
        Icon(
            painter = painterResource(id = id),
            contentDescription = null
        )
    }
}


