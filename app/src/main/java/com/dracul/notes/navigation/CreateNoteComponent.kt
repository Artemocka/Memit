package com.dracul.notes.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.dracul.notes.CreateNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateNoteComponent(
    componentContext: ComponentContext,
    private val onNavigationToScreenB:(String)->Unit,
):ComponentContext by componentContext {


    private var _title = MutableStateFlow("")
    private var _content = MutableStateFlow("")
    val title: StateFlow<String> = _title
    val content: StateFlow<String> = _content

    fun onEvent(event:CreateNoteEvent){
        when(event){
            is CreateNoteEvent.UpdateTitle -> {
                _title.value = event.text
            }
            is CreateNoteEvent.UpdateContent -> {
                _content.value = event.text
            }

            is CreateNoteEvent.Back -> {

            }
        }
    }
}