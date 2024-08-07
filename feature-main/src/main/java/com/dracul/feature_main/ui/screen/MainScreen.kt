package com.dracul.feature_main.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dracul.common.aliases.CommonStrings
import com.dracul.common.utills.getColor
import com.dracul.feature_main.event.MainEvent
import com.dracul.feature_main.event.MainEvent.CreateNote
import com.dracul.feature_main.event.MainEvent.EditNote
import com.dracul.feature_main.event.MainEvent.HideBottomSheet
import com.dracul.feature_main.event.MainEvent.SetNoteColorModal
import com.dracul.feature_main.event.MainEvent.SetSearchQuery
import com.dracul.feature_main.event.MainEvent.SetStarred
import com.dracul.feature_main.event.MainEvent.ShowBottomSheet
import com.dracul.feature_main.event.MainEvent.ShowSearchBar
import com.dracul.feature_main.nav_component.MainComponent
import com.dracul.feature_main.ui.components.ActionsBottomSheet
import com.dracul.feature_main.ui.components.ReminderBottomSheet
import com.dracul.notes.domain.models.Note
import com.mohamedrejeb.richeditor.model.RichTextState


@SuppressLint("NewApi")
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
        { id, pinned -> component.onEvent(SetStarred(id = id, pinned = pinned)) }
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
        initialValue = "", lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    val showReminderDialog by component.showReminderDialog

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { component.onEvent(CreateNote) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add")
        }
    }, topBar = {
        TopAppBarWithSearch(showSearchBox = component.showSearchBar.value,
            text = text.value,
            onEdit = {
                component.onEvent(SetSearchQuery(it))
            }) {
            component.onEvent(ShowSearchBar)
        }
    }) { padding ->
        if (showBottomSheet.value) {
            ActionsBottomSheet(onDismiss = {
                component.onEvent(HideBottomSheet)
            }, onEvent = {
                component.onEvent(it)
            }, onColorClick = {
                component.onEvent(SetNoteColorModal(it))
            }, colorList = component.colorsList
            )
        }
        log(showReminderDialog.toString())
        if (showReminderDialog){
            ReminderBottomSheet {
                component.onEvent(MainEvent.HideReminder)
                it?.let {
                    component.onEvent(MainEvent.CreateReminder(it.timeInMillis))
                }
            }
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
            .combinedClickable(onClick = { onItemClick(item.id) },
                onLongClick = { onItemLongClick(item.id) }),
        colors = CardDefaults.cardColors().copy(containerColor = getColor(item.color)),
        shape = RoundedCornerShape(16.dp),
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
fun ItemGridPreview() {
    val note = Note(0, "Заметка", "Контент", color = 1, pinned = true)
    ItemGrid(item = note, onItemClick = {}, onItemLongClick = {}, onStarClick = { _, _ -> })
}

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

private fun log(msg: String) {
    Log.e(null, msg)
}


