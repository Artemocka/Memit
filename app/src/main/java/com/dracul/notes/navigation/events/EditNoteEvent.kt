package com.dracul.notes.navigation.events


sealed interface EditNoteEvent {
    data class UpdateTitle(val text: String) : EditNoteEvent
    data object Back : EditNoteEvent
    data object SetBold : EditNoteEvent
    data object SetItalic : EditNoteEvent
    data object SetLinethrough : EditNoteEvent
    data object SetUnderline : EditNoteEvent
    data object SetAlignStart : EditNoteEvent
    data object ClearALl : EditNoteEvent
    data object SetAlignCenter : EditNoteEvent
    data object SetAlignEnd : EditNoteEvent
}

