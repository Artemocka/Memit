package com.dracul.feature_main.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dracul.common.aliases.CommonStrings
import com.dracul.common.utills.noRippleClickable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithSearch(
    showSearchBox: Boolean, text: String, onEdit: (String) -> Unit, onClick: () -> Unit
) {
    var maxWidth by remember { mutableStateOf(0.dp) }
    var minWidth by remember { mutableStateOf(0.dp) }
    var target by remember { mutableStateOf(0.dp) }
    var alpha by remember { mutableFloatStateOf(0f) }
    val currentWidth by animateDpAsState(
        targetValue = target, animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow
        ), label = ""
    )
    alpha = ((currentWidth - minWidth) / (maxWidth - minWidth)) * 2.5f
    target = if (showSearchBox) maxWidth else minWidth

    TopAppBar(title = {
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .layout { measurable, constraints ->
                maxWidth = constraints.maxWidth.toDp()
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            }) {
            val (textField) = createRefs()
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(CommonStrings.note), modifier = Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null, modifier = Modifier
                        .wrapContentSize()
                        .noRippleClickable(onClick)
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            minWidth = placeable.width.dp
                            layout(placeable.width, placeable.height) {
                                placeable.place(0, 0)
                            }
                        })
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(currentWidth)
                    .constrainAs(textField) {
                        end.linkTo(parent.end)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (currentWidth >= minWidth + 1.dp) {
                    SearchTextField(
                        modifier = Modifier.alpha(alpha),
                        value = text,
                        onValueChange = onEdit,
                        onClick = onClick,
                    )
                }
            }
        }
    })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(value: String, onValueChange: (String) -> Unit, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val focusRequester = remember { FocusRequester() }
    SideEffect {
        focusRequester.requestFocus()
    }
    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
    BasicTextField(value = value, onValueChange = onValueChange,
        modifier
            .fillMaxWidth()
            .padding(end = 14.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp))
            .focusRequester(focusRequester), textStyle = TextStyle(
        fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface
    ), singleLine = true, cursorBrush = SolidColor(MaterialTheme.colorScheme.outline), decorationBox = {
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = it,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            isError = false,
            colors = colors,
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text(text = stringResource(CommonStrings.search), maxLines = 1) },
            contentPadding = PaddingValues(8.dp),
            interactionSource = interactionSource,
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null, modifier = Modifier.noRippleClickable(onClick))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search, contentDescription = null
                )
            },
        )
    })
}