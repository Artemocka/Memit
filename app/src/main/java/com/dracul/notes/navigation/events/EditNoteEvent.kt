package com.dracul.notes.navigation.events


sealed interface EditNoteEvent {
    data class UpdateTitle(val text: String) : EditNoteEvent
    data class SetColor(val color: Int) : EditNoteEvent
    data object Back : EditNoteEvent
    data object SetStarred : EditNoteEvent
    data object DeleteNote : EditNoteEvent
    data object SetBold : EditNoteEvent
    data object SetItalic : EditNoteEvent
    data object SetLinethrough : EditNoteEvent
    data object SetUnderline : EditNoteEvent
    data object SetAlignStart : EditNoteEvent
    data object ClearALl : EditNoteEvent
    data object SetAlignCenter : EditNoteEvent
    data object SetAlignEnd : EditNoteEvent
    data object ShowColorPicker : EditNoteEvent
    data object HideColorPicker : EditNoteEvent
}

