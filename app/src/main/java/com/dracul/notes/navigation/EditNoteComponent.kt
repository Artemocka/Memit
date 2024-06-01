package com.dracul.notes.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.dracul.notes.data.Note
import com.dracul.notes.navigation.events.EditNoteEvent
import com.example.myapplication.DatabaseProviderWrap

class EditNoteComponent(
    id: Long,
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
) : ComponentContext by componentContext {

    private var note = DatabaseProviderWrap.noteDao.getById(id)
    private var _title = mutableStateOf(note.title)
    private var _content = mutableStateOf(note.content)
    val title: State<String> = _title
    val content: State<String> = _content

    private val backCallback = BackCallback(priority = Int.MAX_VALUE){
        note = note.copy(title = _title.value.trim(), content = _content.value.trim())
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

            is EditNoteEvent.UpdateContent -> {
                _content.value = event.text
            }

            is EditNoteEvent.Back -> {
                note = note.copy(title = _title.value.trim(), content = _content.value.trim())
                note.isEmptyOrUpdate()
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