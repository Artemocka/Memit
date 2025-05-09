package com.dracul.feature_main.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dracul.notes.domain.models.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReminderButton(
    item: Note,
    onReminderClick: (Long) -> Unit,
    onStarClick: (Long, Boolean) -> Unit,
) {
    val starIcon = if (item.pinned) Icons.Filled.Star else Icons.Filled.StarOutline
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        item.workerId?.let {
            Reminder(item, onReminderClick)
        }
        IconButton(
            modifier = Modifier.size(20.dp),
            onClick = { onStarClick(item.id, item.pinned) }
        ) {
            Icon(
                imageVector = starIcon,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun RemindereButtonPreview() {
    ReminderButton(
        item = Note(
            id = 1,
            title = "Заметка",
            content = "Контент заметки",
            color = 1,
            pinned = true,
            workerId = "1",
            reminderTimeStamp = 1L,
        ),
        onReminderClick = {},
        onStarClick = { _, _ -> },
    )
}

@Composable
private fun Reminder(
    item: Note,
    onReminderClick: (Long) -> Unit
) {
    val formatter = SimpleDateFormat("dd MMM, hh:mm", Locale.getDefault())
    val reminderTimeStamp = item.reminderTimeStamp!!
    Box(
        contentAlignment = Alignment.Center,
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

@Preview
@Composable
private fun ReminderPreview() {
    Reminder(
        item = Note(
            id = 1,
            title = "Заметка",
            content = "Контент заметки",
            color = 1,
            pinned = true,
            workerId = "1",
            reminderTimeStamp = 1L,
        )
    ) { }
}