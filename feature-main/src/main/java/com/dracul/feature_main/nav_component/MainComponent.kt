package com.dracul.feature_main.nav_component

import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.arkivanov.decompose.ComponentContext
import com.dracul.common.models.CircleColor
import com.dracul.common.models.CircleColorList
import com.dracul.feature_main.event.MainEvent
import com.dracul.feature_reminder.worker.ReminderWorker
import com.dracul.notes.domain.usecase.DeleteNoteByIdUseCase
import com.dracul.notes.domain.usecase.GetAllNotesUseCase
import com.dracul.notes.domain.usecase.GetNoteByIdUseCase
import com.dracul.notes.domain.usecase.InsertNoteUseCase
import com.dracul.notes.domain.usecase.UpdateNoteUseCase
import com.dracul.notes.domain.usecase.UpdatePinnedNoteByIdUseCase
import com.dracul.notes.domain.usecase.UpdateWorkerByIdUseCase
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    val insertNotesUseCase by inject<InsertNoteUseCase>()
    val updateNotesUseCase by inject<UpdateNoteUseCase>()
    val updatePinnedNoteByIdUseCase by inject<UpdatePinnedNoteByIdUseCase>()
    val updateWorkerByIdUseCase by inject<UpdateWorkerByIdUseCase>()

    private var circleColorList: CircleColorList = CircleColorList()
    private val _showBottomSheet = mutableStateOf(false)
    private var selectedItemId: Long? = null
    private var _colorsList: MutableState<List<CircleColor>> = mutableStateOf(emptyList())
    private var _showSearchBar = mutableStateOf(false)
    private var _searchQuery = MutableStateFlow("")
    private var _showReminderDialog = mutableStateOf(false)
    private var _showReminderDialogWithDelete = mutableStateOf(false)
    var showReminderDialog:State<Boolean> = _showReminderDialog
    var showReminderDialogWithDelete:State<Boolean> = _showReminderDialogWithDelete
    var searchQuery = _searchQuery.asStateFlow()
    var showSearchBar: State<Boolean> = _showSearchBar
    var colorsList: State<List<CircleColor>> = _colorsList
    val showBottomSheet: State<Boolean> = _showBottomSheet
    val notes = combine(_searchQuery, getAllNotesUseCase()) { query, list ->
        if (query.isNotEmpty()) {
            list.filter {
                it.title.contains(
                    query,
                    ignoreCase = true
                ) || RichTextState().setHtml(it.content).annotatedString.text.contains(
                    query,
                    ignoreCase = true
                )
            }.sortedBy { !it.pinned }
        } else {
            list.sortedBy { !it.pinned }
        }
    }


    fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.CreateNote -> {
                onEditNote(null)
                if (_showSearchBar.value) {
                    _searchQuery.value = ""
                    _showSearchBar.value = !_showSearchBar.value
                }
            }

            is MainEvent.EditNote -> {
                onEditNote(event.id)
                if (_showSearchBar.value) {
                    _searchQuery.value = ""
                    _showSearchBar.value = !_showSearchBar.value
                }
            }

            is MainEvent.DeleteNote -> {
                deleteNoteByIdUseCase(event.id)
            }

            is MainEvent.ShowBottomSheet -> {
                selectedItemId = event.id
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

            MainEvent.DeleteNoteModal -> {
                selectedItemId?.let { id ->
                    getNoteByIdUseCase(id)
                }
            }

            MainEvent.DuplicateNoteModal -> {
                selectedItemId?.let { id ->
                    val tempNote = getNoteByIdUseCase(id)
                    insertNotesUseCase(tempNote.copy(id = 0))
                }
            }

            is MainEvent.ShareNoteModal -> {
                selectedItemId?.let { id ->
                    val tempNote = getNoteByIdUseCase(id)
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            if (tempNote.title.isNotEmpty()) "${tempNote.title}\n${
                                RichTextState().setHtml(
                                    tempNote.content
                                ).annotatedString
                            }"
                            else RichTextState().setHtml(tempNote.content).annotatedString
                        )
                        type = "text/plain"
                    }
                    startActivity(
                        event.context,
                        Intent.createChooser(sendIntent, tempNote.title),
                        null
                    )
                }
            }

            MainEvent.HideBottomSheet -> {
                _showBottomSheet.value = false
            }

            MainEvent.EditNoteModal -> {
                selectedItemId?.let {
                    onEditNote(it)
                }
            }

            is MainEvent.SetNoteColorModal -> {
                _colorsList.value = circleColorList.getSelected(event.color)
                selectedItemId?.let {
                    val tempNote =
                        getNoteByIdUseCase(it).copy(color = event.color.color)
                    updateNotesUseCase(tempNote)
                }
            }

            is MainEvent.SetStarred -> {
                updatePinnedNoteByIdUseCase(event.id, !event.pinned)
            }

            is MainEvent.SetSearchQuery -> _searchQuery.value = event.query
            MainEvent.ShowSearchBar -> {
                _showSearchBar.value = !_showSearchBar.value
                if (!_showSearchBar.value)
                    _searchQuery.value = ""
            }

            MainEvent.HideReminder -> {
                _showReminderDialog.value = false
            }
            is MainEvent.ShowReminder -> {
                _showReminderDialog.value = true
            }

            is MainEvent.CreateReminder -> {
                _showReminderDialog.value = false
                _showReminderDialogWithDelete.value = false
                val note = selectedItemId?.let { getNoteByIdUseCase(it) }!!
                val inputData = Data.Builder()
                    .putString("MESSAGE", note.content.let { RichTextState().setHtml(it).annotatedString.text })
                    .putLong("NOTE_ID", note.id)
                    .build()
                val currentTimeInMillis = System.currentTimeMillis()

                val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(event.millis-currentTimeInMillis, TimeUnit.MILLISECONDS)
                    .setInputData(inputData)
                    .addTag("reminder")
                    .build()

                updateWorkerByIdUseCase(note.id, reminderRequest.id.toString(), event.millis)
                WorkManager.getInstance().enqueue(reminderRequest)
            }

            MainEvent.HideReminderWithDelete -> _showReminderDialogWithDelete.value = false
            is MainEvent.ShowReminderWithDelete -> {
                selectedItemId = event.id
                _showReminderDialogWithDelete.value = true
            }
            is MainEvent.DeleteReminder->{
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