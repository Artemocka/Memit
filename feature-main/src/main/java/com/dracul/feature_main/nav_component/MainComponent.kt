package com.dracul.feature_main.nav_component

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.arkivanov.decompose.ComponentContext
import com.dracul.common.models.CircleColor
import com.dracul.common.models.CircleColorList
import com.dracul.feature_main.event.MainAction
import com.dracul.feature_main.event.MainEvent
import com.dracul.feature_reminder.worker.ReminderWorker
import com.dracul.notes.domain.usecase.DeleteNoteByIdUseCase
import com.dracul.notes.domain.usecase.GetAllNotesUseCase
import com.dracul.notes.domain.usecase.GetNoteByIdUseCase
import com.dracul.notes.domain.usecase.UpdateNoteUseCase
import com.dracul.notes.domain.usecase.UpdatePinnedNoteByIdUseCase
import com.dracul.notes.domain.usecase.UpdateWorkerByIdUseCase
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainComponent(
    componentContext: ComponentContext,
    private val onEditNote: (id: Long?) -> Unit,
) : ComponentContext by componentContext, KoinComponent {
    val deleteNoteByIdUseCase by inject<DeleteNoteByIdUseCase>()
    val getNoteByIdUseCase by inject<GetNoteByIdUseCase>()
    val getAllNotesUseCase by inject<GetAllNotesUseCase>()
    val updateNotesUseCase by inject<UpdateNoteUseCase>()
    val updatePinnedNoteByIdUseCase by inject<UpdatePinnedNoteByIdUseCase>()
    val updateWorkerByIdUseCase by inject<UpdateWorkerByIdUseCase>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var circleColorList: CircleColorList = CircleColorList()
    private val _showBottomSheet = mutableStateOf(false)
    private val _events = MutableSharedFlow<MainEvent>(1)
    private var selectedItemId: Long? = null
    private var _colorsList: MutableState<List<CircleColor>> = mutableStateOf(emptyList())
    private var _showSearchBar = mutableStateOf(false)
    private var _searchQuery = MutableStateFlow("")
    private var _showReminderDialog = mutableStateOf(false)
    private var _showReminderDialogWithDelete = mutableStateOf(false)
    var showReminderDialog: State<Boolean> = _showReminderDialog
    var showReminderDialogWithDelete: State<Boolean> = _showReminderDialogWithDelete
    var searchQuery = _searchQuery.asStateFlow()
    var showSearchBar: State<Boolean> = _showSearchBar
    var colorsList: State<List<CircleColor>> = _colorsList
    val showBottomSheet: State<Boolean> = _showBottomSheet
    val events = _events.asSharedFlow()
    val notes = combine(_searchQuery, getAllNotesUseCase()) { query, list ->
        if (query.isNotEmpty()) {
            list.filter {
                it.title.contains(
                    query, ignoreCase = true
                ) || RichTextState().setHtml(it.content).annotatedString.text.contains(
                    query, ignoreCase = true
                )
            }.sortedBy { !it.pinned }
        } else {
            list.sortedBy { !it.pinned }
        }
    }


    fun onAction(action: MainAction) {
        when (action) {
            MainAction.CreateNote -> {
                onEditNote(null)
                if (_showSearchBar.value) {
                    _searchQuery.value = ""
                    _showSearchBar.value = !_showSearchBar.value
                }
            }

            is MainAction.EditNote -> {
                onEditNote(action.id)
                if (_showSearchBar.value) {
                    _searchQuery.value = ""
                    _showSearchBar.value = !_showSearchBar.value
                }
            }

            is MainAction.DeleteNote -> {
                deleteNoteByIdUseCase(action.id)
            }

            is MainAction.ShowBottomSheet -> {
                selectedItemId = action.id
                selectedItemId?.let {
                    val tempNote = getNoteByIdUseCase(it)
                    if (tempNote.color != 0) {
                        _colorsList.value = circleColorList.getSelected(tempNote.color)
                    } else {
                        _colorsList.value = circleColorList.getColors()
                    }
                }
                _showBottomSheet.value = true
            }

            MainAction.DeleteNoteModal -> {
                selectedItemId?.let { id ->
                    getNoteByIdUseCase(id)
                }
            }

            is MainAction.ShareNoteModal -> {
                selectedItemId?.let { id ->
                    coroutineScope.launch {
                        _events.emit(MainEvent.ShareNote(getNoteByIdUseCase(id)))
                    }
                }
            }

            MainAction.HideBottomSheet -> {
                _showBottomSheet.value = false
            }

            MainAction.EditNoteModal -> {
                selectedItemId?.let {
                    onEditNote(it)
                }
            }

            is MainAction.SetNoteColorModal -> {
                _colorsList.value = circleColorList.getSelected(action.color)
                selectedItemId?.let {
                    val tempNote = getNoteByIdUseCase(it).copy(color = action.color.color)
                    updateNotesUseCase(tempNote)
                }
            }

            is MainAction.SetStarred -> {
                updatePinnedNoteByIdUseCase(action.id, !action.pinned)
            }

            is MainAction.SetSearchQuery -> _searchQuery.value = action.query
            MainAction.ShowSearchBar -> {
                _showSearchBar.value = !_showSearchBar.value
                if (!_showSearchBar.value) _searchQuery.value = ""
            }

            MainAction.HideReminder -> {
                _showReminderDialog.value = false
            }

            is MainAction.ShowReminder -> {
                _showReminderDialog.value = true
            }

            is MainAction.CreateReminder -> {
                _showReminderDialog.value = false
                _showReminderDialogWithDelete.value = false
                val note = selectedItemId?.let { getNoteByIdUseCase(it) }!!
                val inputData = Data.Builder().putString("MESSAGE",
                    note.content.let { RichTextState().setHtml(it).annotatedString.text })
                    .putLong("NOTE_ID", note.id).build()
                val currentTimeInMillis = System.currentTimeMillis()

                val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>().setInitialDelay(
                    action.millis - currentTimeInMillis, TimeUnit.MILLISECONDS
                ).setInputData(inputData).addTag("reminder").build()

                updateWorkerByIdUseCase(note.id, reminderRequest.id.toString(), action.millis)
                WorkManager.getInstance().enqueue(reminderRequest)
            }

            MainAction.HideReminderWithDelete -> _showReminderDialogWithDelete.value = false
            is MainAction.ShowReminderWithDelete -> {
                selectedItemId = action.id
                _showReminderDialogWithDelete.value = true
            }

            is MainAction.DeleteReminder -> {
                selectedItemId?.let {
                    val tempNote = getNoteByIdUseCase(it)
                    WorkManager.getInstance().cancelWorkById(UUID.fromString(tempNote.workerId))
                    updateWorkerByIdUseCase(tempNote.id, null, null)
                }
                _showReminderDialogWithDelete.value = false
            }
        }
    }
}