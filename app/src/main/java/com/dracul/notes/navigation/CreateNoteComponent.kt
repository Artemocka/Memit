package com.dracul.notes.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.dracul.notes.data.Note
import com.dracul.notes.navigation.events.CreateNoteEvent
import com.example.myapplication.DatabaseProviderWrap

class CreateNoteComponent(
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
) : ComponentContext by componentContext {


    private var _title = mutableStateOf("")
    private var _content = mutableStateOf("")
    val title: State<String> = _title
    val content: State<String> = _content

    private val backCallback = BackCallback {
        val note = Note(0, _title.value.trim(), _content.value.trim(), 0, false)
        note.isEmptyOrSave()
    }

    init {
        backHandler.register(backCallback)
    }


    fun onEvent(event: CreateNoteEvent) {
        when (event) {
            is CreateNoteEvent.UpdateTitle -> {
                _title.value = event.text
            }

            is CreateNoteEvent.UpdateContent -> {
                _content.value = event.text
            }

            is CreateNoteEvent.Back -> {
                val note = Note(0, _title.value.trim(), _content.value.trim(), 0, false)
                note.isEmptyOrSave()
            }
        }
    }

    private fun Note.isEmptyOrSave() {
        if (this.title.isEmpty() && this.content.isEmpty()) {
            onGoBack()
        } else {
            DatabaseProviderWrap.noteDao.insert(this)
            onGoBack()
        }
    }

}
