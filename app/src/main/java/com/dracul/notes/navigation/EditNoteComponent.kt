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
import com.dracul.pokeapp.utills.poop
import com.example.myapplication.DatabaseProviderWrap
import com.mohamedrejeb.richeditor.model.RichTextState

class EditNoteComponent(
    id: Long,
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
) : ComponentContext by componentContext {

    private var note = DatabaseProviderWrap.noteDao.getById(id)
    private var _title = mutableStateOf(note.title)
    var content = RichTextState().setHtml(note.content)
    val title: State<String> = _title
    val color: State<Int> = mutableIntStateOf(note.color)
    val _formatMode = mutableStateOf(false)
    val formatMode: State<Boolean> = _formatMode

    private val backCallback = BackCallback(priority = Int.MAX_VALUE) {
        note = note.copy(title = _title.value.trim(), content = content.toHtml())
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
                note = note.copy(title = _title.value.trim(), content = content.toHtml())
                note.isEmptyOrUpdate()
            }

            EditNoteEvent.SetBold -> content.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            EditNoteEvent.SetItalic -> content.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            EditNoteEvent.SetStrokethrough -> {
                content.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
            }
            EditNoteEvent.SetAlignCenter -> content.toggleParagraphStyle(ParagraphStyle(TextAlign.Center))
            EditNoteEvent.SetAlignEnd -> content.toggleParagraphStyle(ParagraphStyle(TextAlign.End))
            EditNoteEvent.SetAlignStart -> content.toggleParagraphStyle(ParagraphStyle(TextAlign.Start))
            EditNoteEvent.SetFormatMode -> _formatMode.value = !_formatMode.value
            EditNoteEvent.ClearALl -> {
               content.removeSpanStyle(spanStyle = content.currentSpanStyle)
                content.removeParagraphStyle(paragraphStyle = content.currentParagraphStyle)
            }
        }
    }

    private fun Note.isEmptyOrUpdate() {
        if (this.title.isEmpty() && this.content.isEmpty()) {
            onGoBack()
        } else {
            DatabaseProviderWrap.noteDao.update(this)
            onGoBack()
        }
    }
}