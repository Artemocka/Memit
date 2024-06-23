package com.dracul.notes.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dracul.notes.R
import com.dracul.notes.navigation.EditNoteComponent
import com.dracul.notes.navigation.events.EditNoteEvent
import com.dracul.notes.navigation.events.EditNoteEvent.Back
import com.dracul.notes.navigation.events.EditNoteEvent.UpdateTitle
import com.dracul.notes.ui.components.FormatButtons
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    component: EditNoteComponent
) {
    val title = component.title
    val content = component.content
    val color by component.color
    val isStarred by component.isStarred

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (component.isCreate)"Create" else "Edit") },
                navigationIcon = {
                    IconButton({ component.onEvent(Back) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = getColor(id = color)),
                actions = {
                    IconButton(onClick = { component.onEvent(EditNoteEvent.ShowColorPicker) }) {
                        Icon(imageVector = Icons.Filled.ColorLens  , contentDescription = "Delete")
                    }
                    IconButton(onClick = { component.onEvent(EditNoteEvent.DeleteNote) }) {
                        Icon(imageVector = Icons.Filled.Delete  , contentDescription = "Delete")
                    }
                    IconButton(onClick = { component.onEvent(EditNoteEvent.SetStarred) }) {
                        Icon(imageVector = if (isStarred) Icons.Filled.Star else Icons.Filled.StarOutline , contentDescription = null)
                    }
                }
            )
        }, modifier = Modifier.background(color = getColor(id = color))
    ) { padding ->

        if (component.showColorDialog.value){
            ColorPickerDialog(currentColor = color, onDismiss = { component.onEvent(EditNoteEvent.HideColorPicker)}) {
                component.onEvent(EditNoteEvent.SetColor(it))
            }
        }
        
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

            FormatButtons(
                isFocused = isFocused,
                content = content,
                component = component,
                color = color
            )

        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPickerDialog(currentColor:Int,onDismiss:()->Unit,setColor:(Int)->Unit) {
    Dialog(onDismissRequest = onDismiss ) {
        Card(
            colors = CardDefaults.cardColors().copy(containerColor = getBlendedCardColor(id = currentColor)),
            shape = RoundedCornerShape(32.dp)
        ) {
            FlowRow(modifier = Modifier.padding(8.dp), Arrangement.Center) {
                repeat(12){colorId->
                    val iconColor = getColor(id = colorId)
                    AnimatedContent(targetState =  (currentColor == colorId),
                        transitionSpec = {
                            scaleIn(
                                tween(200, easing = EaseIn),
                                initialScale = 0.95f
                            ) + fadeIn(initialAlpha = 0.9f) togetherWith scaleOut(
                                tween(200, easing = EaseOut),
                                targetScale = 0.85f
                            ) + fadeOut(targetAlpha = 0.9f)
                        }) {
                        if (it) {
                            Image(painter = painterResource(id = R.drawable.ic_selected_circle),
                                colorFilter = ColorFilter.tint(iconColor),
                                contentDescription = "Color circle",
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .noRippleClickable { setColor(colorId) }
                            )
                        } else {
                            Image(painter = painterResource(id = R.drawable.ic_circle),
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

