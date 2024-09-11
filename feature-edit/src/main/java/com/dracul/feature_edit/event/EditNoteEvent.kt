package com.dracul.feature_edit.event



sealed interface EditNoteEvent {
    data object ShowMediaRequest : EditNoteEvent
}

