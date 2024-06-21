package com.dracul.notes.navigation.events

import android.content.Context
import com.dracul.notes.data.CircleColor

sealed interface MainEvent {
    data class EditNote(val id: Long) : MainEvent
    data class DeleteNote(val id: Long) : MainEvent
    data class SetStarred(val id:Long, val pinned:Boolean) : MainEvent
    data class SetSearchQuery(val query:String) : MainEvent
    data class SetNoteColorModal(val color: CircleColor) : MainEvent
    data class ShowBottomSheet(val id: Long) : MainEvent
    data class ShareNoteModal(val context: Context) : MainEvent
    data object ShowSearchBar : MainEvent
    data object HideBottomSheet : MainEvent
    data object EditNoteModal : MainEvent
    data object DeleteNoteModal : MainEvent
    data object DuplicateNoteModal : MainEvent
    data object CreateNote : MainEvent
}