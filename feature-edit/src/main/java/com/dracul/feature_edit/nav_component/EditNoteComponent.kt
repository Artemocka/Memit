package com.dracul.feature_edit.nav_component

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
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

    private var note: Note = id.let {
        if (it == null) {
            return@let Note(0, "", "", 0)
        } else {
            return@let getNoteByIdUseCase(it)
        }
    }

    private var _title = mutableStateOf(note.title)
    private val _color = mutableIntStateOf(note.color)
    private val _showColorDialog = mutableStateOf(false)
    private var _isStarred = mutableStateOf(note.pinned)
    private var _events = MutableSharedFlow<EditNoteEvent>(0)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var history: History = History(RichTextState().setHtml(note.content).copy())
    private val backCallback = BackCallback(priority = Int.MAX_VALUE) {
        note = note.copy(
            title = _title.value.trim(),
            content = content.value.toHtml(),
            pinned = _isStarred.value,
            color = _color.intValue
        )
        if (note.id.toInt() == 0) note.isEmptyOrInsert()
        else note.isEmptyOrUpdate()
    }

    val images = getAllImagesByParentId(note.id)
    val events: SharedFlow<EditNoteEvent> = _events
    val isCreate: Boolean = note.id.toInt() == 0
    var isStarred: State<Boolean> = _isStarred
    val showColorDialog: State<Boolean> = _showColorDialog
    var content = mutableStateOf(RichTextState().setHtml(note.content).copy())
    val title: State<String> = _title
    val color: State<Int> = _color

    init {
        backHandler.register(backCallback)
        coroutineScope.launch {
            while (true) {
                if (history.current.value.value.annotatedString.hashCode() != content.value.annotatedString.hashCode()) {
                    history.add(content.value.copy())
                }
                delay(750)
            }
        }
    }

    val isHasNext: State<Boolean> = history.isHasNext
    val isHasPrev: State<Boolean> = history.isHasPrev
    fun onEvent(action: EditNoteAction) {
        when (action) {
            is EditNoteAction.UpdateTitle -> {
                _title.value = action.text
            }

            is EditNoteAction.Back -> {
                note = note.copy(
                    title = _title.value.trim(),
                    content = content.value.toHtml(),
                    pinned = _isStarred.value,
                    color = _color.intValue
                )
                if (note.id.toInt() == 0) note.isEmptyOrInsert()
                else note.isEmptyOrUpdate()
            }

            EditNoteAction.SetBold -> content.value.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            EditNoteAction.SetItalic -> content.value.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            EditNoteAction.SetLinethrough -> content.value.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
            EditNoteAction.SetAlignCenter -> content.value.toggleParagraphStyle(
                ParagraphStyle(
                    TextAlign.Center
                )
            )

            EditNoteAction.SetAlignEnd -> content.value.toggleParagraphStyle(
                ParagraphStyle(
                    TextAlign.End
                )
            )

            EditNoteAction.SetAlignStart -> content.value.toggleParagraphStyle(
                ParagraphStyle(
                    TextAlign.Start
                )
            )

            EditNoteAction.ClearALl -> {
                content.value.currentSpanStyle.textDecoration?.let {
                    if (it.contains(TextDecoration.Underline)) content.value.toggleSpanStyle(
                        SpanStyle(textDecoration = TextDecoration.Underline)
                    )
                    if (it.contains(TextDecoration.LineThrough)) content.value.toggleSpanStyle(
                        SpanStyle(textDecoration = TextDecoration.LineThrough)
                    )
                }
                content.value.toggleSpanStyle(content.value.currentSpanStyle)
                content.value.removeParagraphStyle(paragraphStyle = content.value.currentParagraphStyle)
            }

            EditNoteAction.SetUnderline -> {
                content.value.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            }

            EditNoteAction.SetStarred -> {
                _isStarred.value = !_isStarred.value
            }

            EditNoteAction.DeleteNote -> {
                if (note.id == 0.toLong()) {
                    onGoBack()
                } else {
                    deleteNoteUseCase(item = note)
                    onGoBack()
                }
            }

            EditNoteAction.ShowColorPicker -> _showColorDialog.value = true
            EditNoteAction.HideColorPicker -> _showColorDialog.value = false
            is EditNoteAction.SetColor -> _color.intValue = action.color
            EditNoteAction.Redo -> {
                history.next()
                content.value = history.current.value.value.copy()
            }

            EditNoteAction.Undo -> {
                history.prev()
                content.value = history.current.value.value.copy()
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

            is EditNoteAction.DeleteImage -> {
                deleteImageUseCase(action.image)
            }

            is EditNoteAction.ShowImage -> {

                onViewer(
                    note.id, action.index
                )
            }
        }
    }

    private fun Note.isEmptyOrUpdate() {
        if (this.title.isEmpty() && this.content == "<br>") {
            onGoBack()
        } else {
            updateNoteUseCase(this)
            onGoBack()
        }
    }

    private fun Note.isEmptyOrInsert() {
        if (this.title.isEmpty() && this.content == "<br>") {
            onGoBack()
        } else {
            insertNoteUseCase(this)
            onGoBack()
        }
    }
}