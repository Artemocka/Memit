package com.dracul.feature_main.ui.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dracul.feature_main.event.MainAction.CreateNote
import com.dracul.feature_main.event.MainAction.CreateReminder
import com.dracul.feature_main.event.MainAction.DeleteReminder
import com.dracul.feature_main.event.MainAction.EditNote
import com.dracul.feature_main.event.MainAction.HideBottomSheet
import com.dracul.feature_main.event.MainAction.HideReminder
import com.dracul.feature_main.event.MainAction.HideReminderWithDelete
import com.dracul.feature_main.event.MainAction.SetNoteColorModal
import com.dracul.feature_main.event.MainAction.SetSearchQuery
import com.dracul.feature_main.event.MainAction.SetStarred
import com.dracul.feature_main.event.MainAction.ShowBottomSheet
import com.dracul.feature_main.event.MainAction.ShowReminderWithDelete
import com.dracul.feature_main.event.MainAction.ShowSearchBar
import com.dracul.feature_main.event.MainEvent
import com.dracul.feature_main.nav_component.MainComponent
import com.dracul.feature_main.ui.components.ActionsBottomSheet
import com.dracul.feature_main.ui.components.ItemGrid
import com.dracul.feature_main.ui.components.ReminderBottomSheet
import com.dracul.feature_main.ui.components.ReminderBottomSheetWithDelete
import com.dracul.feature_main.ui.components.TopAppBarWithSearch
import com.mohamedrejeb.richeditor.model.RichTextState


@SuppressLint("NewApi")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    component: MainComponent
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val clickLambda = remember<(id: Long) -> Unit> {
        { component.onAction(EditNote(it)) }
    }
    val starClickLambda = remember<(id: Long, pinned: Boolean) -> Unit> {
        { id, pinned -> component.onAction(SetStarred(id = id, pinned = pinned)) }
    }
    val longClickLambda = remember<(id: Long) -> Unit> {
        {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            component.onAction(ShowBottomSheet(it))
        }
    }
    val reminderClickLambda = remember<(id: Long) -> Unit> {
        {
            component.onAction(ShowReminderWithDelete(it))
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
    LaunchedEffect(Unit) {
        component.events.collect {
            when (it) {
                is MainEvent.ShareNote -> {
                    val tempNote = it.note
                    Log.e(null, "asd")
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            if (tempNote.title.isNotEmpty()) "${tempNote.title}\n${
                                RichTextState().setHtml(
                                    tempNote.content
                                ).annotatedString
                            }"
                            else RichTextState().setHtml(tempNote.content).annotatedString
                        )
                        type = "text/plain"
                    }
                    startActivity(
                        context, Intent.createChooser(sendIntent, tempNote.title), null
                    )
                }
            }
        }
    }
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { component.onAction(CreateNote) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add")
        }
    }, topBar = {
        TopAppBarWithSearch(showSearchBox = component.showSearchBar.value,
            text = text.value,
            onEdit = {
                component.onAction(SetSearchQuery(it))
            }) {
            component.onAction(ShowSearchBar)
        }
    }) { padding ->
        if (showBottomSheet.value) {
            ActionsBottomSheet(onDismiss = {
                component.onAction(HideBottomSheet)
            }, onAction = {
                component.onAction(it)
            }, onColorClick = {
                component.onAction(SetNoteColorModal(it))
            }, colorList = component.colorsList
            )
        }
        if (showReminderDialog) {
            ReminderBottomSheet(onDismissRequest = {
                component.onAction(HideReminder)
            }) {
                it?.let {
                    component.onAction(CreateReminder(it.timeInMillis))
                }
            }
        }

        if (component.showReminderDialogWithDelete.value) {
            ReminderBottomSheetWithDelete(onDismissRequest = {
                component.onAction(HideReminderWithDelete)
            }, onCreateReminder = {
                component.onAction(CreateReminder(it.timeInMillis))
            }) {
                component.onAction(DeleteReminder)
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
                    onReminderClick = reminderClickLambda
                )
            }
        }
    }
}



