package com.dracul.notes.navigation

import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import com.arkivanov.decompose.ComponentContext
import com.dracul.notes.data.CircleColor
import com.dracul.notes.data.CircleColorList
import com.dracul.notes.navigation.events.MainEvent
import com.example.myapplication.DatabaseProviderWrap
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class MainComponent(
    componentContext: ComponentContext,
    private val onEditNote: (id: Long?) -> Unit,
) : ComponentContext by componentContext {

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
    val notes = combine(_searchQuery, DatabaseProviderWrap.noteDao.getAll()) { query, list ->
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
                DatabaseProviderWrap.noteDao.deleteById(event.id)
            }

            is MainEvent.ShowBottomSheet -> {
                selectedItemId = event.id
                selectedItemId?.let {
                    val tempNote = DatabaseProviderWrap.noteDao.getById(it)
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
                    DatabaseProviderWrap.noteDao.deleteById(id)
                }
            }

            MainEvent.DuplicateNoteModal -> {
                selectedItemId?.let { id ->
                    val tempNote = DatabaseProviderWrap.noteDao.getById(id)
                    DatabaseProviderWrap.noteDao.insert(tempNote.copy(id = 0))
                }
            }

            is MainEvent.ShareNoteModal -> {
                selectedItemId?.let { id ->
                    val tempNote = DatabaseProviderWrap.noteDao.getById(id)
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
                        DatabaseProviderWrap.noteDao.getById(it).copy(color = event.color.color)
                    DatabaseProviderWrap.noteDao.update(tempNote)
                }
            }

            is MainEvent.SetStarred -> {
                DatabaseProviderWrap.noteDao.updatePinnedById(event.id, !event.pinned)
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