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

class MainComponent(
    componentContext: ComponentContext,
    private val onCreateNote: () -> Unit,
    private val onEditNote: (id: Long) -> Unit,
) : ComponentContext by componentContext {

    private var circleColorList: CircleColorList = CircleColorList()
    private val _showBottomSheet = mutableStateOf(false)
    private var selectedItemId: Long? = null
    private var _colorsList: MutableState<List<CircleColor>> = mutableStateOf(emptyList())
    var colorsList: State<List<CircleColor>> = _colorsList
    val showBottomSheet: State<Boolean> = _showBottomSheet
    val notes = DatabaseProviderWrap.noteDao.getAll()


    fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.CreateNote -> {
                onCreateNote()
            }

            is MainEvent.EditNote -> {
                onEditNote(event.id)
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
                            if (tempNote.title.isNotEmpty()) "${tempNote.title}\n${tempNote.content}"
                            else "${RichTextState().setMarkdown(tempNote.content).annotatedString}"
                        )
                        type = "text/plain"
                    }
                    startActivity(event.context, Intent.createChooser(sendIntent, tempNote.title), null)
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
                    val tempNote = DatabaseProviderWrap.noteDao.getById(it).copy(color = event.color.color)
                    DatabaseProviderWrap.noteDao.update(tempNote)
                }
            }
        }
    }

}