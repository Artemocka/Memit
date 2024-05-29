package com.dracul.notes.navigation.events

sealed interface CreateNoteEvent {
    data class UpdateTitle(val text: String) : CreateNoteEvent
    data class UpdateContent(val text: String) : CreateNoteEvent
    data object Back : CreateNoteEvent
}