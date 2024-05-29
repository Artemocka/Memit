package com.dracul.notes.navigation.events

import android.content.Context

sealed interface MainEvent {
    data class EditNote(val id: Long) : MainEvent
    data class DeleteNote(val id: Long) : MainEvent
    data class ShowBottomSheet(val id: Long) : MainEvent
    data object HideBottomSheet : MainEvent
    data object EditNoteModal : MainEvent
    data object DeleteNoteModal : MainEvent
    data object DuplicateNoteModal : MainEvent
    data class ShareNoteModal(val context: Context) : MainEvent
    data object CreateNote : MainEvent
}