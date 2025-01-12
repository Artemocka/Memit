package com.dracul.feature_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dracul.feature_edit.history.History
import com.dracul.images.domain.models.Image
import com.dracul.notes.domain.models.Note
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.Flow

class EditNoteState(
    val images: Flow<List<Image>>,
    note: Note,
    color: Int,
    pinned: Boolean,
    showColorDialog: Boolean,
    history: History,
    isCreate: Boolean,
    content: RichTextState,
    title: String,
) {
    var note by mutableStateOf(note)
    var color by mutableIntStateOf(color)
    var pinned by mutableStateOf(pinned)
    var showColorDialog by mutableStateOf(showColorDialog)
    var history by mutableStateOf(history)
    var isCreate by mutableStateOf(isCreate)
    var content by mutableStateOf(content)
    var title by mutableStateOf(title)
}