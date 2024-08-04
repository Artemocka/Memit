package com.dracul.feature_main.nav_component

import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import com.arkivanov.decompose.ComponentContext
import com.dracul.common.models.CircleColor
import com.dracul.common.models.CircleColorList
import com.dracul.feature_main.event.MainEvent
import com.dracul.notes.domain.usecase.DeleteNoteByIdUseCase
import com.dracul.notes.domain.usecase.DeleteNoteUseCase
import com.dracul.notes.domain.usecase.GetAllNotesUseCase
import com.dracul.notes.domain.usecase.GetNoteByIdUseCase
import com.dracul.notes.domain.usecase.InsertNoteUseCase
import com.dracul.notes.domain.usecase.UpdateNoteUseCase
import com.dracul.notes.domain.usecase.UpdatePinnedNoteByIdUseCase
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainComponent(
    componentContext: ComponentContext,
    private val onEditNote: (id: Long?) -> Unit,
) : ComponentContext by componentContext, KoinComponent {
    val deleteNoteUseCase by inject<DeleteNoteUseCase>()
    val deleteNoteByIdUseCase by inject<DeleteNoteByIdUseCase>()
    val getNoteByIdUseCase by inject<GetNoteByIdUseCase>()
    val getAllNotesUseCase by inject<GetAllNotesUseCase>()
    val insertNotesUseCase by inject<InsertNoteUseCase>()
    val updateNotesUseCase by inject<UpdateNoteUseCase>()
    val updatePinnedNoteByIdUseCase by inject<UpdatePinnedNoteByIdUseCase>()

    private var circleColorList: CircleColorList = CircleColorList()
    private val _showBottomSheet = mutableStateOf(false)
    private var selectedItemId: Long? = null
    private var _colorsList: MutableState<List<CircleColor>> = mutableStateOf(emptyList())
    private var _showSearchBar = mutableStateOf(false)
    private var _searchQuery = MutableStateFlow("")
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
                selectedItemId = null
                _showBottomSheet.value = false
            }

            MainEvent.EditNoteModal -> {
                selectedItemId?.let {
                    onEditNote(it)
                }
                selectedItemId = null
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
        }
    }

}