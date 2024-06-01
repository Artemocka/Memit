package com.dracul.notes.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    component: MainComponent
) {

    val clickLambda = remember<(id: Long) -> Unit> {
        { component.onEvent(EditNote(it)) }
    }
    val longClickLambda = remember<(id: Long) -> Unit> {
        { component.onEvent(ShowBottomSheet(it)) }
    }
    val notes = component.notes.collectAsStateWithLifecycle(initialValue = emptyList(), lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current )
    val showBottomSheet = component.showBottomSheet
    if (showBottomSheet.value) {
        BottomSheet(
            onDismiss = {
                component.onEvent(HideBottomSheet)
            },
            onClick = {
                component.onEvent(it)
            },
            onColorClick = {
                component.onEvent(MainEvent.SetNoteColorModal(it))
            },
            colorList = component.colorsList

        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { component.onEvent(CreateNote) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        },
        topBar = {
            TopAppBar(title = { Text(text = "Notes") })
        }
    ) { padding ->

        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(horizontal = 16.dp),
            columns = StaggeredGridCells.Adaptive(180.dp),
            contentPadding = padding,
        ) {

            items(
                count = notes.value.size,
                key = {
                    notes.value[it].id
                }
            ) { index ->
                ItemGrid(
                    item = notes.value[index],
                    onItemClick = clickLambda,
                    onItemLongClick = longClickLambda,
                )
            }
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
    val haptic = LocalHapticFeedback.current
    val clickLambda = remember {
        { onItemClick(item.id) }
    }
    val longClickLambda = remember {
        {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onItemLongClick(item.id)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = clickLambda,
                onLongClick = longClickLambda
            ),
        colors = if (item.color != 0) CardDefaults.cardColors().copy(containerColor = getColor(item.color)) else CardDefaults.cardColors(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)

    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = item.title,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.content,
                maxLines = 8,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    onClick: (event: MainEvent) -> Unit,
    onColorClick: (CircleColor) -> Unit,
    colorList: State<List<CircleColor>>,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val localContext = LocalContext.current
    val editLambda = remember<() -> Unit> {
        { onClick(EditNoteModal) }
    }
    val duplicateLambda = remember<() -> Unit> {
        { onClick(DuplicateNoteModal) }
    }
    val shareLambda = remember<() -> Unit> {
        { onClick(ShareNoteModal(localContext)) }
    }
    val deleteLambda = remember<() -> Unit> {
        { onClick(DeleteNoteModal) }
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
                .horizontalScroll(scrollState)
        ) {
            repeat(colorList.value.size){
                CircleColorItem(item = colorList.value[it], onClick = onColorClick)
            }
        }
        BottomSheetRow(image = Icons.Filled.Edit, text = "Edit", onClick = editLambda)
        BottomSheetRow(image = painterResource(id = R.drawable.ic_copy), text = "Duplicate", onClick = duplicateLambda)
        BottomSheetRow(image = Icons.Filled.Share, text = "Share", onClick = shareLambda)
        BottomSheetRow(image = Icons.Filled.Delete, text = "Delete", onClick = deleteLambda)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun BottomSheetRow(image: Painter, text: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        Row(modifier = Modifier.padding(vertical = 16.dp)) {
            Image(
                image, contentDescription = text,
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
fun CircleColorItem(item: CircleColor, onClick: (CircleColor) -> Unit) {
    val color = getColor(id = item.color)
    Image(
        painter = painterResource(id = if (item.selected) R.drawable.ic_selected_circle else R.drawable.ic_circle),
        colorFilter = ColorFilter.tint(color),
        contentDescription = "Color circle",
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(CircleShape)
            .clickable { onClick(item) }
    )
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
        else -> Color(0)
    }
}
