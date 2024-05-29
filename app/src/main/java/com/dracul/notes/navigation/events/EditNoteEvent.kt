package com.dracul.notes.navigation.events

sealed interface EditNoteEvent {
    data class UpdateTitle(val text: String) : EditNoteEvent
    data class UpdateContent(val text: String) : EditNoteEvent
    data object Back : EditNoteEvent
}