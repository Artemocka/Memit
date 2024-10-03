package com.dracul.feature_main.event

import com.dracul.common.models.CircleColor

sealed interface MainAction {
    data class EditNote(val id: Long) : MainAction
    data class ViewImage(val id: Long, val index:Int) : MainAction
    data class DeleteNote(val id: Long) : MainAction
    data class SetStarred(val id: Long, val pinned: Boolean) : MainAction
    data class SetSearchQuery(val query: String) : MainAction
    data class SetNoteColorModal(val color: CircleColor) : MainAction
    data class ShowBottomSheet(val id: Long) : MainAction
    data object ShareNoteModal : MainAction
    data class CreateReminder(val millis: Long) : MainAction
    data object ShowSearchBar : MainAction
    data object HideBottomSheet : MainAction
    data object EditNoteModal : MainAction
    data object DeleteNoteModal : MainAction
    data object CreateNote : MainAction
    data object ShowReminder : MainAction
    data object HideReminder : MainAction
    data class ShowReminderWithDelete(val id: Long) : MainAction
    data object HideReminderWithDelete : MainAction
    data object DeleteReminder : MainAction
}