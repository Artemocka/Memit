package com.dracul.notes.navigation

sealed interface CreateNoteEvent {
    data class UpdateTitle(val text: String) : CreateNoteEvent
    data class UpdateContent(val text: String) : CreateNoteEvent
    data class Back(val text: String) : CreateNoteEvent
}