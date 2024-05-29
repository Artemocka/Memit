package com.dracul.notes.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dracul.notes.R
import com.dracul.notes.db.Note
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


@Composable
fun MainScreen(
    component: MainComponent
) {
    val notes = component.notes.collectAsStateWithLifecycle(initialValue = emptyList())
    val showBottomSheet = component.showBottomSheet
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { component.onEvent(CreateNote) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        },
    ) { padding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            columns = StaggeredGridCells.Adaptive(180.dp),
            contentPadding = padding,
        ) {
            items(notes.value.size) { note ->
                ItemGrid(
                    item = notes.value[note],
                    onItemClick = { id ->
                        component.onEvent(EditNote(id))
                    },
                    onItemLongClick = { id ->
                        component.onEvent(ShowBottomSheet(id))
                    },
                )
            }
        }
        if (showBottomSheet.value) {
            BottomSheet(
                onDismiss = {
                    component.onEvent(HideBottomSheet)
                },
                onClick = {
                    component.onEvent(it)
                }
            )
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemGrid(
    item: Note,
    onItemClick: (Long) -> Unit,
    onItemLongClick: (Long) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .combinedClickable(
                onClick = {
                    onItemClick(item.id)
                },
                onLongClick = {
                    onItemLongClick(item.id)
                }
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = item.title)
            Text(text = item.content)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onDismiss: () -> Unit, onClick: (event: MainEvent) -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val localContext = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        BottomSheetRow(image = Icons.Filled.Edit, text = "Edit", onClick = { onClick(EditNoteModal) })
        BottomSheetRow(image = painterResource(id = R.drawable.ic_copy), text = "Duplicate", onClick = { onClick(DuplicateNoteModal) })
        BottomSheetRow(image = Icons.Filled.Share, text = "Share", onClick = { onClick(ShareNoteModal(localContext)) })
        BottomSheetRow(image = Icons.Filled.Delete, text = "Delete", onClick = { onClick(DeleteNoteModal) })
    }
}

@Composable
fun BottomSheetRow(image: Painter, text: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        Row(modifier = Modifier.padding(vertical = 12.dp)) {
            Image(
                image, contentDescription = text,
                Modifier.padding(start = 16.dp, end = 16.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Text(text = text)
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
                image, contentDescription = text,
                Modifier.padding(start = 16.dp, end = 16.dp),
                colorFilter = if (text == "Delete") ColorFilter.tint(MaterialTheme.colorScheme.error) else ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Text(
                text = text,
                color = if (text == "Delete") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}
