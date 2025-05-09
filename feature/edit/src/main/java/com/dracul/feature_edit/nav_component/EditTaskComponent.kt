package com.dracul.feature_edit.nav_component

import com.arkivanov.decompose.ComponentContext
import com.dracul.feature_edit.event.EditTaskAction
import com.dracul.feature_edit.event.EditTaskEvent
import com.dracul.feature_edit.ui.state.EditTaskState
import com.dracul.images.domain.usecase.GetAllImagesByParentIdUseCase
import com.dracul.task.domain.models.Task
import com.dracul.task.domain.usecase.task.GetTaskByIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditTaskComponent(
    id: Long?,
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
    private val onViewer: (parentId: Long, index: Int) -> Unit,
) : ComponentContext by componentContext, KoinComponent {

    private val getTaskByIdUseCase by inject<GetTaskByIdUseCase>()
    private val getAllImagesByParentId by inject<GetAllImagesByParentIdUseCase>()
    private val task =
        if (id == null) Task(id = 0, title = "", color = 0) else getTaskByIdUseCase(id)

    private var _events = MutableSharedFlow<EditTaskEvent>(0)
    private var _state = EditTaskState(
        images = getAllImagesByParentId(task.id),
        task = task,
        color = task.color,
        pinned = task.pinned,
        showColorDialog = false,
        isCreate = id == null,
        title = task.title,
    )
    var state = _state
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    val events: SharedFlow<EditTaskEvent> = _events


    fun onEvent(action: EditTaskAction) {
        when (action) {
            EditTaskAction.AddImage -> {}
            EditTaskAction.CloseScreen -> {}
            is EditTaskAction.DeleteImage -> {}
            EditTaskAction.DeleteTask -> {}
            EditTaskAction.HideColorPicker -> {}
            is EditTaskAction.SelectImage -> {}
            is EditTaskAction.SetColor -> {}
            EditTaskAction.ShowColorPicker -> {}
            is EditTaskAction.ShowImage -> {}
            is EditTaskAction.UpdateTitle -> {}
            EditTaskAction.Back -> {}
            EditTaskAction.SetPinned -> {}
        }
    }

    //    private fun save() {
    //        val note = state.note.copy(
    //            title = state.title.trim(),
    //            content = state.content.toHtml(),
    //            pinned = state.pinned,
    //            color = state.color
    //        )
    //        if (state.note.id.toInt() == 0) note.isEmptyOrInsert() else note.isEmptyOrUpdate()
    //    }
    //
    //    private fun Note.isEmptyOrUpdate() = updateNoteUseCase(this)
    //
    //    private fun Note.isEmptyOrInsert() = insertNoteUseCase(this)

}