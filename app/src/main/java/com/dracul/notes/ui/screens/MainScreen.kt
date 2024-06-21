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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dracul.notes.R
import com.dracul.notes.data.CircleColor
import com.dracul.notes.data.Note
import com.dracul.notes.navigation.MainComponent
import com.dracul.notes.navigation.events.MainEvent
import com.dracul.notes.navigation.events.MainEvent.CreateNote
import com.dracul.notes.navigation.events.MainEvent.DeleteNoteModal
import com.dracul.notes.navigation.events.MainEvent.DuplicateNoteModal
import com.dracul.notes.navigation.events.MainEvent.EditNote
import com.dracul.notes.navigation.events.MainEvent.EditNoteModal
import com.dracul.notes.navigation.events.MainEvent.HideBottomSheet
import com.dracul.notes.navigation.events.MainEvent.ShareNoteModal
import com.dracul.notes.navigation.events.MainEvent.ShowBottomSheet
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    component: MainComponent
) {
    val haptic = LocalHapticFeedback.current
    val clickLambda = remember<(id: Long) -> Unit> {
        { component.onEvent(EditNote(it)) }
    }
    val starClickLambda = remember<(id: Long, pinned: Boolean) -> Unit> {
        { id, pinned -> component.onEvent(MainEvent.SetStarred(id = id, pinned = pinned)) }
    }
    val longClickLambda = remember<(id: Long) -> Unit> {
        {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            component.onEvent(ShowBottomSheet(it))
        }
    }
    val notes = component.notes.collectAsStateWithLifecycle(
        initialValue = emptyList(),
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    val showBottomSheet = component.showBottomSheet
    val text = component.searchQuery.collectAsStateWithLifecycle(
        initialValue = "",
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { component.onEvent(CreateNote) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }, topBar = {
            TopAppBarWithSearch(
                showSearchBox = component.showSearchBar.value,
                text = text.value,
                onEdit = {
                    component.onEvent(MainEvent.SetSearchQuery(it))
                }) {
                component.onEvent(MainEvent.ShowSearchBar)
            }
        }) { padding ->
        if (showBottomSheet.value) {
            BottomSheet(onDismiss = {
                component.onEvent(HideBottomSheet)
            }, onEvent = {
                component.onEvent(it)
            }, onColorClick = {
                component.onEvent(MainEvent.SetNoteColorModal(it))
            }, colorList = component.colorsList
            )
        }
        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(horizontal = 8.dp),
            columns = StaggeredGridCells.Adaptive(180.dp),
            contentPadding = padding,
        ) {

            items(count = notes.value.size, key = {
                notes.value[it].id
            }) { index ->
                ItemGrid(
                    modifier = Modifier.animateItemPlacement(tween(200)),
                    item = notes.value[index],
                    onItemClick = clickLambda,
                    onItemLongClick = longClickLambda,
                    onStarClick = starClickLambda,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemGrid(
    modifier: Modifier = Modifier,
    item: Note,
    onItemClick: (Long) -> Unit,
    onItemLongClick: (Long) -> Unit,
    onStarClick: (Long, Boolean) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = { onItemClick(item.id) }, onLongClick = { onItemLongClick(item.id) }
            ),
        colors = if (item.color != 0) CardDefaults.cardColors()
            .copy(containerColor = getColor(item.color)) else CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            if (item.color == 0) 0.5.dp else 0.dp,
            if (item.color == 0) MaterialTheme.colorScheme.outline else Color.Transparent
        )

    ) {
        Row(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f)
                    .fillMaxSize()
            ) {
                if (item.title.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = item.title,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = RichTextState().setHtml(item.content).annotatedString,
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                )
            }
            IconButton(
                onClick = { onStarClick(item.id, item.pinned) },
            ) {
                Icon(
                    imageVector = if (item.pinned) Icons.Filled.Star else Icons.Filled.StarOutline,
                    contentDescription = null
                )
            }
        }

    }
}

@Preview
@Composable
fun ItemGrinPreview() {
    val note = Note(0, "Заметка", "Контент", color = 1, pinned = true)
    ItemGrid(item = note, onItemClick = {}, onItemLongClick = {}, onStarClick = { a, b -> })
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithSearch(
    showSearchBox: Boolean,
    text: String,
    onEdit: (String) -> Unit,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Unspecified,
        unfocusedContainerColor = Color.Unspecified,
        disabledContainerColor = Color.Unspecified,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
    TopAppBar(
        title = {
            if (showSearchBox) {
                Row(
                    modifier = Modifier.padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SideEffect {
                        focusRequester.requestFocus()
                    }
                    BasicTextField(
                        value = text,
                        onValueChange = onEdit,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 48.sp,
                            color = MaterialTheme.colorScheme.onSurface
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
                                placeholder = { Text(text = "Search ") },
                                contentPadding = PaddingValues(10.dp),
                                shape = RoundedCornerShape(16.dp),
                                interactionSource = interactionSource,
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = null,
                                        modifier = Modifier.clickable {
                                            onClick()
                                        })
                                },
                            )
                        }
                    )


                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Note", modifier = Modifier.weight(1f))
                    IconButton(onClick = onClick) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                    }
                }
            }

        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    onEvent: (event: MainEvent) -> Unit,
    onColorClick: (CircleColor) -> Unit,
    colorList: State<List<CircleColor>>,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()


    val editLambda = remember<() -> Unit> {
        { onEvent(EditNoteModal) }
    }
    val duplicateLambda = remember<() -> Unit> {
        { onEvent(DuplicateNoteModal) }
    }
    val shareLambda = remember<() -> Unit> {
        { onEvent(ShareNoteModal(localContext)) }
    }
    val deleteLambda = remember<() -> Unit> {
        { onEvent(DeleteNoteModal) }
    }


    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = WindowInsets(bottom = 0),

        ) {
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),

            ) {
            repeat(colorList.value.size) {
                when (it) {
                    0 -> {
                        CircleColorItem(
                            modifier = Modifier.padding(start = 8.dp),
                            item = colorList.value[it],
                            onClick = onColorClick
                        )
                    }

                    colorList.value.lastIndex -> {
                        CircleColorItem(
                            modifier = Modifier.padding(end = 8.dp),
                            item = colorList.value[it],
                            onClick = onColorClick
                        )
                    }

                    else -> {
                        CircleColorItem(item = colorList.value[it], onClick = onColorClick)
                    }
                }
            }
        }
        BottomSheetRow(image = Icons.Filled.Edit, text = "Edit", onClick = {
            editLambda()
            scope.launch {
                modalBottomSheetState.hide()
            }.invokeOnCompletion {
                onEvent(HideBottomSheet)

            }
        })
        BottomSheetRow(
            image = painterResource(id = R.drawable.ic_copy),
            text = "Duplicate",
            onClick = {
                duplicateLambda()
                scope.launch {
                    modalBottomSheetState.hide()
                }.invokeOnCompletion {
                    onEvent(HideBottomSheet)

                }
            })
        BottomSheetRow(image = Icons.Filled.Share, text = "Share", onClick = {
            shareLambda()
            scope.launch {
                modalBottomSheetState.hide()
            }.invokeOnCompletion {
                onEvent(HideBottomSheet)

            }
        })
        BottomSheetRow(image = Icons.Filled.Delete, text = "Delete", onClick = {
            deleteLambda()
            scope.launch {
                modalBottomSheetState.hide()
            }.invokeOnCompletion {
                onEvent(HideBottomSheet)

            }
        })
        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
fun BottomSheetRow(image: Painter, text: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        Row(modifier = Modifier.padding(vertical = 16.dp)) {
            Image(
                image,
                contentDescription = text,
                Modifier.padding(start = 16.dp, end = 16.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Text(
                text = text,
                color = if (text == "Delete") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun CircleColorItem(
    modifier: Modifier = Modifier,
    item: CircleColor,
    onClick: (CircleColor) -> Unit
) {
    val color = getColor(id = item.color)
    AnimatedContent(targetState = item.selected,
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
                colorFilter = ColorFilter.tint(color),
                contentDescription = "Color circle",
                modifier = modifier
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .noRippleClickable { onClick(item) }
            )
        } else {
            Image(painter = painterResource(id = R.drawable.ic_circle),
                colorFilter = ColorFilter.tint(color),
                contentDescription = "Color circle",
                modifier = modifier
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .clickable { onClick(item) })
        }
    }
}


@Composable
fun BottomSheetRow(image: ImageVector, text: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        Row(modifier = Modifier.padding(vertical = 16.dp)) {
            Image(
                image,
                contentDescription = text,
                Modifier.padding(start = 16.dp, end = 16.dp),
                colorFilter = if (text == "Delete") ColorFilter.tint(MaterialTheme.colorScheme.error) else ColorFilter.tint(
                    MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = text,
                color = if (text == "Delete") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun getColor(id: Int): Color {
    return when (id) {
        1 -> colorResource(R.color.color1)
        2 -> colorResource(R.color.color2)
        3 -> colorResource(R.color.color3)
        4 -> colorResource(R.color.color4)
        5 -> colorResource(R.color.color5)
        6 -> colorResource(R.color.color6)
        7 -> colorResource(R.color.color7)
        8 -> colorResource(R.color.color8)
        9 -> colorResource(R.color.color9)
        10 -> colorResource(R.color.color10)
        11 -> colorResource(R.color.color11)
        12 -> colorResource(R.color.color12)
        else -> MaterialTheme.colorScheme.background
    }
}

@Composable
fun getBlendedColor(id: Int): Color {
    val color = getColor(id = id)
    val white = colorResource(id = R.color.white)
    return lerp(color, white, 0.35f)
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

