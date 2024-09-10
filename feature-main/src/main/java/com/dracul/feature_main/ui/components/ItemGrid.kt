package com.dracul.feature_main.ui.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.dracul.common.utills.getColor
import com.dracul.images.domain.usecase.GetAllImagesByParentIdUseCase
import com.dracul.notes.domain.models.Note
import com.mohamedrejeb.richeditor.model.RichTextState
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemGrid(
    modifier: Modifier = Modifier,
    item: Note,
    onItemClick: (Long) -> Unit,
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
    val context = LocalContext.current
    val scrollState = rememberScrollState()


    LaunchedEffect(color) {
        animatedColor.animateTo(color, animationSpec = tween(300, easing = EaseInOutCubic))
    }
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(onClick = { onItemClick(item.id) },
                onLongClick = { onItemLongClick(item.id) }),
        colors = CardDefaults.cardColors().copy(containerColor = animatedColor.value),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            Modifier
                .fillMaxSize()

        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                ,
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {

                item.workerId?.let {
                    val formatter = SimpleDateFormat("dd MMM, hh:mm", java.util.Locale.getDefault())
                    val reminderTimeStamp = item.reminderTimeStamp!!
                    Box(contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .wrapContentWidth()
                            .height(21.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                BorderStroke(
                                    1.5.dp, MaterialTheme.colorScheme.onSurfaceVariant
                                ), RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                onReminderClick(item.id)
                            }) {
                        Text(

                            modifier = Modifier.padding(horizontal = 6.dp),
                            fontSize = 11.sp,
                            text = formatter.format(Date(item.reminderTimeStamp!!)),
                            fontWeight = FontWeight(600),
                            textDecoration = if (System.currentTimeMillis() > reminderTimeStamp) TextDecoration.LineThrough else null
                        )
                    }

                }
                IconButton(
                    onClick = { onStarClick(item.id, item.pinned) }, modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = if (item.pinned) Icons.Filled.Star else Icons.Filled.StarOutline,
                        contentDescription = null,
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 8.dp)

            ) {
                if (item.title.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp, top = 4.dp),
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
            if (images.isNotEmpty()){
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                        .height(48.dp)
                ) {
                    repeat(images.size) {
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context).data(images[it].uri)
                                .size(Size.ORIGINAL).memoryCacheKey(images[it].id.hashCode().toString())
                                .diskCacheKey(images[it].id.hashCode().toString())
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED).build()
                        )
                        Image(
                            modifier = Modifier.then(
                                when (it) {
                                    0 -> Modifier.padding(start = 6.dp)
                                    images.lastIndex -> Modifier.padding(end = 6.dp)
                                    else -> Modifier.padding()
                                }
                            ).padding(horizontal = 3.dp).size(48.dp, 48.dp).clip(RoundedCornerShape(8.dp)),
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun ItemGridPreview() {
    val note = Note(0, "Заметка", "Контент", color = 1, pinned = true)
    ItemGrid(item = note,
        onItemClick = {},
        onItemLongClick = {},
        onStarClick = { _, _ -> },
        onReminderClick = {})
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemGridV1(
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
        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Top,

                ) {

                IconButton(
                    onClick = { onStarClick(item.id, item.pinned) },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddAlert,
                        contentDescription = null,
                    )
                }
                IconButton(
                    onClick = { onStarClick(item.id, item.pinned) }, modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = if (item.pinned) Icons.Filled.Star else Icons.Filled.StarOutline,
                        contentDescription = null,
                    )
                }

            }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (item.title.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp, top = 4.dp),
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
        }

    }
}
