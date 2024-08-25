package com.dracul.feature_main.ui.screen

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.dracul.feature_main.ui.components.ItemGrid
import com.dracul.feature_main.ui.components.ReminderBottomSheet
import com.dracul.feature_main.ui.components.ReminderBottomSheetWithDelete
import com.dracul.feature_main.ui.components.TopAppBarWithSearch


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
    val reminderClickLambda = remember<(id: Long) -> Unit> {
        {
            component.onEvent(MainEvent.ShowReminderWithDelete(it))
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
        TopAppBarWithSearch(
            showSearchBox = component.showSearchBar.value,
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
        if (showReminderDialog) {
            ReminderBottomSheet(onDismissRequest = {
                component.onEvent(MainEvent.HideReminder)
            }){
                it?.let {
                    component.onEvent(MainEvent.CreateReminder(it.timeInMillis))
                }
            }
        }

        if (component.showReminderDialogWithDelete.value) {
            ReminderBottomSheetWithDelete(onDismissRequest = {
                component.onEvent(MainEvent.HideReminderWithDelete)
            }, onCreateReminder = {
                    component.onEvent(MainEvent.CreateReminder(it.timeInMillis))
            }){
                component.onEvent(MainEvent.DeleteReminder)
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


private fun log(msg: String) {
    Log.e(null, msg)
}


