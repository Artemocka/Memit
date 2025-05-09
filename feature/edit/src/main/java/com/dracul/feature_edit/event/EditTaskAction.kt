package com.dracul.feature_edit.event

import android.net.Uri
import com.dracul.images.domain.models.Image

sealed interface EditTaskAction {
    data class DeleteImage(val image: Image) : EditTaskAction
    data class SelectImage(val uri: Uri) : EditTaskAction
    data class SetColor(val color: Int) : EditTaskAction
    data class ShowImage(val index: Int) : EditTaskAction
    data class UpdateTitle(val text: String) : EditTaskAction
    data object AddImage : EditTaskAction
    data object Back : EditTaskAction
    data object CloseScreen : EditTaskAction
    data object DeleteTask : EditTaskAction
    data object HideColorPicker : EditTaskAction
    data object SetPinned : EditTaskAction
    data object ShowColorPicker : EditTaskAction
}