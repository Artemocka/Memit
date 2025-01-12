package com.dracul.feature_edit.nav_component

import androidx.compose.runtime.State
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.arkivanov.decompose.ComponentContext
import com.dracul.feature_edit.EditNoteState
import com.dracul.feature_edit.event.EditNoteAction
import com.dracul.feature_edit.event.EditNoteEvent
import com.dracul.feature_edit.history.History
import com.dracul.images.domain.models.Image
import com.dracul.images.domain.usecase.DeleteImageUseCase
import com.dracul.images.domain.usecase.GetAllImagesByParentIdUseCase
import com.dracul.images.domain.usecase.InsertImageUseCase
import com.dracul.notes.domain.models.Note
import com.dracul.notes.domain.usecase.DeleteNoteUseCase
import com.dracul.notes.domain.usecase.GetNoteByIdUseCase
import com.dracul.notes.domain.usecase.InsertNoteUseCase
import com.dracul.notes.domain.usecase.UpdateNoteUseCase
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditNoteComponent(
    id: Long?,
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
    private val onViewer: (parentId: Long, index: Int) -> Unit,
) : ComponentContext by componentContext, KoinComponent {

    private val getNoteByIdUseCase by inject<GetNoteByIdUseCase>()
    private val insertNoteUseCase by inject<InsertNoteUseCase>()
    private val updateNoteUseCase by inject<UpdateNoteUseCase>()
    private val deleteNoteUseCase by inject<DeleteNoteUseCase>()
    private val insertImageUseCase by inject<InsertImageUseCase>()
    private val deleteImageUseCase by inject<DeleteImageUseCase>()
    private val getAllImagesByParentId by inject<GetAllImagesByParentIdUseCase>()
    private val note = if (id == null) Note(0, "", "", 0) else getNoteByIdUseCase(id)

    var state = EditNoteState(
        note = note,
        title = note.title,
        color = note.color,
        history = History(RichTextState().setHtml(note.content).copy()),
        images = getAllImagesByParentId(note.id),
        isCreate = id == null,
        pinned = note.pinned,
        showColorDialog = false,
        content = RichTextState().setHtml(note.content),
    )

    private var _events = MutableSharedFlow<EditNoteEvent>(0)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val events: SharedFlow<EditNoteEvent> = _events

    init {
        coroutineScope.launch {
            while (true) {
                if (state.history.current.value.value.annotatedString.hashCode() != state.content.annotatedString.hashCode()) {
                    val history = state.history
                    history.add(state.content.copy())
                    state.history = history
                }
                delay(750)
            }
        }
    }

    val isHasNext: State<Boolean> = state.history.isHasNext
    val isHasPrev: State<Boolean> = state.history.isHasPrev

    fun onEvent(action: EditNoteAction) {
        when (action) {
            is EditNoteAction.UpdateTitle -> state.title = action.text
            is EditNoteAction.SetColor -> state.color = action.color
            is EditNoteAction.Back -> {
                val note = state.note.copy(title = state.title.trim(), content = state.content.toHtml(), pinned = state.pinned, color = state.color)
                if (note.id.toInt() == 0) note.isEmptyOrInsert() else note.isEmptyOrUpdate()
                onGoBack()
            }
            EditNoteAction.SetBold -> {
                state.content.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            }
            EditNoteAction.SetItalic -> {
                state.content.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            }
            EditNoteAction.SetLinethrough -> {
                state.content.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
            }
            EditNoteAction.SetAlignCenter -> {
                state.content.toggleParagraphStyle(ParagraphStyle(TextAlign.Center))
            }
            EditNoteAction.SetAlignEnd -> {
                state.content.toggleParagraphStyle(ParagraphStyle(TextAlign.End))
            }
            EditNoteAction.SetAlignStart -> {
                state.content.toggleParagraphStyle(ParagraphStyle(TextAlign.End))
            }
            EditNoteAction.ClearALl -> {
                val content = state.content.copy()
                content.currentSpanStyle.textDecoration?.let {
                    if (it.contains(TextDecoration.Underline)) content.toggleSpanStyle(
                        SpanStyle(textDecoration = TextDecoration.Underline)
                    )
                    if (it.contains(TextDecoration.LineThrough)) content.toggleSpanStyle(
                        SpanStyle(textDecoration = TextDecoration.LineThrough)
                    )
                }
                content.toggleSpanStyle(content.currentSpanStyle)
                content.removeParagraphStyle(paragraphStyle = content.currentParagraphStyle)
                state.content = content
            }
            EditNoteAction.SetUnderline -> {
                state.content.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            }
            EditNoteAction.SetPinned -> {
                state.pinned = !state.pinned
            }
            EditNoteAction.DeleteNote -> {
                if (note.id == 0.toLong()) {
                    onGoBack()
                } else {
                    deleteNoteUseCase(item = note)
                    onGoBack()
                }
            }
            EditNoteAction.ShowColorPicker -> state.showColorDialog = true
            EditNoteAction.HideColorPicker -> state.showColorDialog = false
            EditNoteAction.Redo -> {
                state.history.next()
                state.content = state.history.current.value.value.copy()
            }
            EditNoteAction.Undo -> {
                state.history.prev()
                state.content = state.history.current.value.value.copy()
            }
            EditNoteAction.AddImage -> {
                coroutineScope.launch {
                    _events.emit(EditNoteEvent.ShowMediaRequest)
                }
            }
            is EditNoteAction.SelectImage -> {
                val image = Image(
                    id = 0, parentId = note.id, uri = action.uri
                )
                insertImageUseCase(image)
            }
            is EditNoteAction.DeleteImage -> deleteImageUseCase(action.image)
            is EditNoteAction.ShowImage -> onViewer(note.id, action.index)
            EditNoteAction.CloseScreen -> if (state.title.isNotEmpty() || state.content.toMarkdown().isNotEmpty()) save()
        }
    }

    private fun save() {
        val note = state.note.copy(
            title = state.title.trim(), content = state.content.toHtml(), pinned = state.pinned, color = state.color
        )
        if (state.note.id.toInt() == 0) note.isEmptyOrInsert() else note.isEmptyOrUpdate()
    }

    private fun Note.isEmptyOrUpdate() {
        updateNoteUseCase(this)
    }

    private fun Note.isEmptyOrInsert() {
        insertNoteUseCase(this)
    }
}