package com.dracul.feature_main.ui.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dracul.common.utills.getColor
import com.dracul.images.domain.models.Image
import com.dracul.images.domain.usecase.GetAllImagesByParentIdUseCase
import com.dracul.notes.domain.models.Note
import com.mohamedrejeb.richeditor.model.RichTextState
import org.koin.compose.koinInject
import java.util.Calendar

@Composable
fun ItemGrid(
    modifier: Modifier = Modifier,
    item: Note,
    onItemClick: (Long) -> Unit,
    onImageClick: (id: Long, index: Int) -> Unit,
    onItemLongClick: (Long) -> Unit,
    onStarClick: (Long, Boolean) -> Unit,
    onReminderClick: (Long) -> Unit,
    getAllImagesByParentIdUseCase: GetAllImagesByParentIdUseCase = koinInject()
) {
    val calendar = Calendar.getInstance()
    item.reminderTimeStamp?.let {
        calendar.timeInMillis = it
    }
    val color = getColor(id = item.color)
    val animatedColor = remember { Animatable(color) }
    val images by getAllImagesByParentIdUseCase(item.id).collectAsState(emptyList())

    LaunchedEffect(color) {
        animatedColor.animateTo(color, animationSpec = tween(300, easing = EaseInOutCubic))
    }

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = { onItemClick(item.id) },
                onLongClick = { onItemLongClick(item.id) }),
        colors = CardDefaults.cardColors().copy(containerColor = animatedColor.value),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            ReminderButton(
                item = item,
                onReminderClick = onReminderClick,
                onStarClick = onStarClick,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                if (item.title.isNotEmpty())
                    Title(item)
                Content(item = item, images = images)
            }
            if (images.isNotEmpty())
                ImageRow(
                    images = images,
                    id = item.id,
                    onImageClick = onImageClick,
                )
        }
    }
}

@Composable
private fun Content(
    item: Note,
    images: List<Image>
) {
    Text(
        modifier = if (images.isEmpty()) Modifier.padding(bottom = 4.dp) else Modifier,
        text = RichTextState().setHtml(item.content).annotatedString,
        maxLines = 8,
        overflow = TextOverflow.Ellipsis,
        fontSize = 16.sp,
    )
}

@Composable
private fun Title(item: Note) {
    Text(
        modifier = Modifier.padding(bottom = 4.dp, top = 4.dp),
        text = item.title,
        fontSize = 18.sp,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun ImageRow(
    images: List<Image>,
    id: Long,
    onImageClick: (id: Long, index: Int) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .height(48.dp),
    ) {
        repeat(images.size) { imageIndex ->
            AsyncImage(
                noteId = id,
                images = images,
                imageIndex = imageIndex,
                onImageClick = onImageClick
            )
        }
    }
}


@Preview
@Composable
fun ItemGridPreview() {
    Note(
        id = 0,
        title = "Заметка",
        content = "Контент",
        color = 1,
        pinned = true
    )
}
