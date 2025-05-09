package com.dracul.feature_edit.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dracul.images.domain.models.Image
import com.dracul.task.domain.models.Task
import kotlinx.coroutines.flow.Flow

class EditTaskState(
    val images: Flow<List<Image>>,
    task: Task,
    color: Int,
    pinned: Boolean,
    showColorDialog: Boolean,
    isCreate: Boolean,
    title: String,
) {
    var task by mutableStateOf(task)
    var color by mutableIntStateOf(color)
    var pinned by mutableStateOf(pinned)
    var showColorDialog by mutableStateOf(showColorDialog)
    var isCreate by mutableStateOf(isCreate)
    var title by mutableStateOf(title)
}