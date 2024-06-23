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
import com.example.myapplication.DatabaseProviderWrap
import com.mohamedrejeb.richeditor.model.RichTextState

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

    private var _isStarred = mutableStateOf(note.pinned)
    var isStarred: State<Boolean> = _isStarred
    private var _title = mutableStateOf(note.title)
    private val _color = mutableIntStateOf(note.color)
    private val _showColorDialog= mutableStateOf(false)
    val showColorDialog:State<Boolean> = _showColorDialog
    var content = RichTextState().setHtml(note.content)
    val title: State<String> = _title
    val color: State<Int> =_color
    private val backCallback = BackCallback(priority = Int.MAX_VALUE) {
        note = note.copy(
            title = _title.value.trim(),
            content = content.toHtml(),
            pinned = _isStarred.value,
             color = _color.intValue
        )
        if (note.id.toInt() == 0)
            note.isEmptyOrInsert()
        else
            note.isEmptyOrUpdate()
    }

    init {
        backHandler.register(backCallback)
    }

    fun onEvent(event: EditNoteEvent) {
        when (event) {
            is EditNoteEvent.UpdateTitle -> {
                _title.value = event.text
            }

            is EditNoteEvent.Back -> {
                note = note.copy(
                    title = _title.value.trim(),
                    content = content.toHtml(),
                    pinned = _isStarred.value,
                    color = _color.intValue
                )
                if (note.id.toInt() == 0)
                    note.isEmptyOrInsert()
                else
                    note.isEmptyOrUpdate()
            }

            EditNoteEvent.SetBold -> content.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            EditNoteEvent.SetItalic -> content.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            EditNoteEvent.SetLinethrough -> content.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
            EditNoteEvent.SetAlignCenter -> content.toggleParagraphStyle(ParagraphStyle(TextAlign.Center))
            EditNoteEvent.SetAlignEnd -> content.toggleParagraphStyle(ParagraphStyle(TextAlign.End))
            EditNoteEvent.SetAlignStart -> content.toggleParagraphStyle(ParagraphStyle(TextAlign.Start))
            EditNoteEvent.ClearALl -> {
                content.currentSpanStyle.textDecoration?.let {
                    if (it.contains(TextDecoration.Underline))
                        content.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                    if (it.contains(TextDecoration.LineThrough))
                        content.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                }
                content.toggleSpanStyle(content.currentSpanStyle)
                content.removeParagraphStyle(paragraphStyle = content.currentParagraphStyle)
            }

            EditNoteEvent.SetUnderline -> {
                content.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
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