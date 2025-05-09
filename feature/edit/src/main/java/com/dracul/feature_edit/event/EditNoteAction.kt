package com.dracul.feature_edit.event

import android.net.Uri
import com.dracul.images.domain.models.Image

sealed interface EditNoteAction {
    data class DeleteImage(val image: Image) : EditNoteAction
    data class SelectImage(val uri: Uri) : EditNoteAction
    data class SetColor(val color: Int) : EditNoteAction
    data class ShowImage(val index: Int) : EditNoteAction
    data class UpdateTitle(val text: String) : EditNoteAction
    data object AddImage : EditNoteAction
    data object Back : EditNoteAction
    data object ClearALl : EditNoteAction
    data object CloseScreen : EditNoteAction
    data object DeleteNote : EditNoteAction
    data object HideColorPicker : EditNoteAction
    data object Redo : EditNoteAction
    data object SetAlignCenter : EditNoteAction
    data object SetAlignEnd : EditNoteAction
    data object SetAlignStart : EditNoteAction
    data object SetBold : EditNoteAction
    data object SetItalic : EditNoteAction
    data object SetLinethrough : EditNoteAction
    data object SetPinned : EditNoteAction
    data object SetUnderline : EditNoteAction
    data object ShowColorPicker : EditNoteAction
    data object Undo : EditNoteAction
}