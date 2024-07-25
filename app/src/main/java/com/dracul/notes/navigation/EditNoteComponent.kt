package com.dracul.notes.navigation

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
import com.dracul.notes.data.Note
import com.dracul.notes.navigation.events.EditNoteEvent
import com.dracul.notes.utills.History
import com.example.myapplication.DatabaseProviderWrap
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditNoteComponent(
    id: Long?,
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
) : ComponentContext by componentContext {

    private var note: Note = id.let {
        if (it == null) {
            return@let Note(0, "", "", 0)
        } else {
            return@let DatabaseProviderWrap.noteDao.getById(it)
        }
    }
    val isCreate: Boolean = note.id.toInt() == 0
    private var _isStarred = mutableStateOf(note.pinned)
    var isStarred: State<Boolean> = _isStarred
    private var _title = mutableStateOf(note.title)
    private val _color = mutableIntStateOf(note.color)
    private val _showColorDialog = mutableStateOf(false)
    val showColorDialog: State<Boolean> = _showColorDialog
    var content = mutableStateOf(RichTextState().setHtml(note.content).copy())
    val title: State<String> = _title
    val color: State<Int> = _color
    private val backCallback = BackCallback(priority = Int.MAX_VALUE) {
        note = note.copy(
            title = _title.value.trim(),
            content = content.value.toHtml(),
            pinned = _isStarred.value,
            color = _color.intValue
        )
        if (note.id.toInt() == 0)
            note.isEmptyOrInsert()
        else
            note.isEmptyOrUpdate()
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var history:History = History(RichTextState().setHtml(note.content).copy())

    init {
        backHandler.register(backCallback)
        coroutineScope.launch {
            while (true){
                if (
                    history.current.value.value.annotatedString.hashCode() != content.value.annotatedString.hashCode()
                ){
                    history.add(content.value.copy())
                }
                delay(750 )
            }
        }
    }
    val isHasNext:State<Boolean> = history.isHasNext
    val isHasPrev:State<Boolean> = history.isHasPrev
    fun onEvent(event: EditNoteEvent) {
        when (event) {
            is EditNoteEvent.UpdateTitle -> {
                _title.value = event.text
            }

            is EditNoteEvent.Back -> {
                note = note.copy(
                    title = _title.value.trim(),
                    content = content.value.toHtml(),
                    pinned = _isStarred.value,
                    color = _color.intValue
                )
                if (note.id.toInt() == 0)
                    note.isEmptyOrInsert()
                else
                    note.isEmptyOrUpdate()
            }

            EditNoteEvent.SetBold -> content.value.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            EditNoteEvent.SetItalic -> content.value.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            EditNoteEvent.SetLinethrough -> content.value.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
            EditNoteEvent.SetAlignCenter -> content.value.toggleParagraphStyle(ParagraphStyle(TextAlign.Center))
            EditNoteEvent.SetAlignEnd -> content.value.toggleParagraphStyle(ParagraphStyle(TextAlign.End))
            EditNoteEvent.SetAlignStart -> content.value.toggleParagraphStyle(ParagraphStyle(TextAlign.Start))
            EditNoteEvent.ClearALl -> {
                content.value.currentSpanStyle.textDecoration?.let {
                    if (it.contains(TextDecoration.Underline))
                        content.value.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                    if (it.contains(TextDecoration.LineThrough))
                        content.value.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                }
                content.value.toggleSpanStyle(content.value.currentSpanStyle)
                content.value.removeParagraphStyle(paragraphStyle = content.value.currentParagraphStyle)
            }

            EditNoteEvent.SetUnderline -> {
                content.value.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            }

            EditNoteEvent.SetStarred -> {
                _isStarred.value = !_isStarred.value
            }

            EditNoteEvent.DeleteNote -> {
                if (note.id == 0.toLong()) {
                    onGoBack()
                } else {
                    DatabaseProviderWrap.noteDao.delete(item = note)
                    onGoBack()
                }
            }

            EditNoteEvent.ShowColorPicker -> _showColorDialog.value = true
            EditNoteEvent.HideColorPicker -> _showColorDialog.value = false
            is EditNoteEvent.SetColor -> _color.intValue = event.color
            EditNoteEvent.Redo -> {
                history.next()
                content.value = history.current.value.value.copy()
            }
            EditNoteEvent.Undo -> {
                history.prev()
                content.value = history.current.value.value.copy()
            }
        }
    }

    private fun Note.isEmptyOrUpdate() {
        if (this.title.isEmpty() && this.content == "<br>") {
            onGoBack()
        } else {
            DatabaseProviderWrap.noteDao.update(this)
            onGoBack()
        }
    }

    private fun Note.isEmptyOrInsert() {
        if (this.title.isEmpty() && this.content == "<br>") {
            onGoBack()
        } else {
            DatabaseProviderWrap.noteDao.insert(this)
            onGoBack()
        }
    }
}