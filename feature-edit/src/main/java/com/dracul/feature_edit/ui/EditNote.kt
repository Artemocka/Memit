package com.dracul.feature_edit.ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dracul.common.aliases.CommonStrings
import com.dracul.common.utills.getBlendedColor
import com.dracul.common.utills.getColor
import com.dracul.feature_edit.event.EditNoteEvent.Back
import com.dracul.feature_edit.event.EditNoteEvent.DeleteNote
import com.dracul.feature_edit.event.EditNoteEvent.HideColorPicker
import com.dracul.feature_edit.event.EditNoteEvent.SetColor
import com.dracul.feature_edit.event.EditNoteEvent.SetStarred
import com.dracul.feature_edit.event.EditNoteEvent.ShowColorPicker
import com.dracul.feature_edit.event.EditNoteEvent.UpdateTitle
import com.dracul.feature_edit.nav_component.EditNoteComponent
import com.dracul.feature_edit.ui.components.ColorPickerDialog
import com.dracul.feature_edit.ui.components.FormatButtons
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    component: EditNoteComponent
) {
    val title = component.title
    val content = component.content
    val colorId by component.color
    val color = getColor(id = colorId)
    val animatedColor = remember { Animatable(color) }

    val isStarred by component.isStarred


    LaunchedEffect(color) {
        animatedColor.animateTo(color, animationSpec = tween(500, easing = EaseInOutCubic))
    }
    Scaffold(
        containerColor = animatedColor.value, topBar = {
            TopAppBar(title = {
                Text(
                    text = if (component.isCreate) stringResource(CommonStrings.create) else stringResource(
                        CommonStrings.create
                    )
                )
            },
                navigationIcon = {
                    IconButton({ component.onEvent(Back) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = Color.Transparent),
                actions = {
                    IconButton(onClick = { component.onEvent(ShowColorPicker) }) {
                        Icon(imageVector = Icons.Filled.ColorLens, contentDescription = "Delete")
                    }
                    IconButton(onClick = { component.onEvent(DeleteNote) }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                    }
                    IconButton(onClick = { component.onEvent(SetStarred) }) {
                        Icon(
                            imageVector = if (isStarred) Icons.Filled.Star else Icons.Filled.StarOutline,
                            contentDescription = null
                        )
                    }
                })
        }, modifier = Modifier.background(color = Color.Transparent)
    ) { padding ->

        if (component.showColorDialog.value) {
            ColorPickerDialog(
                currentColor = colorId,
                onDismiss = { component.onEvent(HideColorPicker) }) {
                component.onEvent(SetColor(it))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .padding(horizontal = 8.dp)
                .navigationBarsPadding(),
        ) {
            OutlinedTextField(
                placeholder = { Text(text = stringResource(CommonStrings.title_optional)) },
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
                state = content.value,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(Color.Transparent),
                interactionSource = interactionSource,
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                colors = RichTextEditorDefaults.richTextEditorColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    selectionColors = TextSelectionColors(
                        getBlendedColor(id = colorId), getBlendedColor(id = colorId)
                    ),
                ),
                placeholder = { Text(text = stringResource(CommonStrings.content)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Default,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                ),
            )
            FormatButtons(
                isFocused = isFocused,
                content = content.value,
                component = component,
                color = colorId
            )
        }
    }
}



