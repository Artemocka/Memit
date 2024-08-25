package com.dracul.feature_main.event

import android.content.Context
import com.dracul.common.models.CircleColor

sealed interface MainEvent {
    data class EditNote(val id: Long) : MainEvent
    data class DeleteNote(val id: Long) : MainEvent
    data class SetStarred(val id: Long, val pinned: Boolean) : MainEvent
    data class SetSearchQuery(val query: String) : MainEvent
    data class SetNoteColorModal(val color: CircleColor) : MainEvent
    data class ShowBottomSheet(val id: Long) : MainEvent
    data class ShareNoteModal(val context: Context) : MainEvent
    data class CreateReminder(val millis: Long) : MainEvent
    data object ShowSearchBar : MainEvent
    data object HideBottomSheet : MainEvent
    data object EditNoteModal : MainEvent
    data object DeleteNoteModal : MainEvent
    data object DuplicateNoteModal : MainEvent
    data object CreateNote : MainEvent
    data object ShowReminder : MainEvent
    data object HideReminder : MainEvent
    data class ShowReminderWithDelete(val id: Long) : MainEvent
    data object HideReminderWithDelete : MainEvent
    data object DeleteReminder : MainEvent
}