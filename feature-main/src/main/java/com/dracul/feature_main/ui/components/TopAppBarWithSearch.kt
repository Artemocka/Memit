package com.dracul.feature_main.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dracul.common.aliases.CommonStrings


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithSearch(
    showSearchBox: Boolean, text: String, onEdit: (String) -> Unit, onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val colors = TextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
    TopAppBar(title = {
        if (showSearchBox) {
            Row(
                modifier = Modifier.padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SideEffect {
                    focusRequester.requestFocus()
                }
                BasicTextField(value = text,
                    onValueChange = onEdit,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    decorationBox = {
                        TextFieldDefaults.DecorationBox(
                            value = text,
                            innerTextField = it,
                            enabled = true,
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            isError = false,
                            colors = colors,
                            placeholder = { Text(text = stringResource(CommonStrings.search)) },
                            contentPadding = PaddingValues(8.dp),
                            shape = RoundedCornerShape(16.dp),
                            interactionSource = interactionSource,
                            trailingIcon = {
                                Icon(imageVector = Icons.Filled.Close,
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        onClick()
                                    })
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search, contentDescription = null
                                )
                            },
                        )
                    })
            }
        } else {
            Row(
                modifier = Modifier.padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(CommonStrings.note), modifier = Modifier.weight(1f))
                IconButton(onClick = onClick) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                }
            }
        }
    })
}