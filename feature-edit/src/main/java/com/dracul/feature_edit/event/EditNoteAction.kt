package com.dracul.feature_edit.event

import android.net.Uri
import com.dracul.images.domain.models.Image


sealed interface EditNoteAction {
    data class UpdateTitle(val text: String) : EditNoteAction
    data class SetColor(val color: Int) : EditNoteAction
    data object Back : EditNoteAction
    data object SetStarred : EditNoteAction
    data object DeleteNote : EditNoteAction
    data object SetBold : EditNoteAction
    data object SetItalic : EditNoteAction
    data object SetLinethrough : EditNoteAction
    data object SetUnderline : EditNoteAction
    data object SetAlignStart : EditNoteAction
    data object ClearALl : EditNoteAction
    data object SetAlignCenter : EditNoteAction
    data object SetAlignEnd : EditNoteAction
    data object ShowColorPicker : EditNoteAction
    data object HideColorPicker : EditNoteAction
    data object Undo : EditNoteAction
    data object Redo : EditNoteAction
    data object AddImage : EditNoteAction
    data class DeleteImage(val image: Image) : EditNoteAction
    data class SelectImage(val uri: Uri) : EditNoteAction
    data class ShowImage(val index:Int) : EditNoteAction
}

