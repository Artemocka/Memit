package com.dracul.notes.navigation

import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import com.arkivanov.decompose.ComponentContext
import com.dracul.notes.db.Note
import com.dracul.notes.navigation.events.MainEvent
import com.example.myapplication.DatabaseProviderWrap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainComponent(
    componentContext: ComponentContext,
    private val onCreateNote: () -> Unit,
    private val onEditNote: (id: Long) -> Unit,
) : ComponentContext by componentContext {


    private val _showBottomSheet = mutableStateOf(false)
    private var selectedItem: Long? = null
    val showBottomSheet: State<Boolean> = _showBottomSheet
    lateinit var notes: Flow<List<Note>>
    val job = CoroutineScope(Dispatchers.IO).launch {
        notes = DatabaseProviderWrap.noteDao.getAll()
    }

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
                selectedItem = event.id
                _showBottomSheet.value = true
            }

            MainEvent.DeleteNoteModal -> {
                selectedItem?.let { id ->
                    DatabaseProviderWrap.noteDao.deleteById(id)
                }
                _showBottomSheet.value = false
            }

            MainEvent.DuplicateNoteModal -> {
                selectedItem?.let { id ->
                    val tempNote = DatabaseProviderWrap.noteDao.getById(id)
                    DatabaseProviderWrap.noteDao.insert(tempNote.copy(id = 0))
                }
                _showBottomSheet.value = false
            }

            is MainEvent.ShareNoteModal -> {
                selectedItem?.let { id ->
                    val tempNote = DatabaseProviderWrap.noteDao.getById(id)
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "${tempNote.title}\n${tempNote.content}")
                        type = "text/plain"
                    }

                    startActivity(event.context, Intent.createChooser(sendIntent, tempNote.title), null)
                }


            }

            MainEvent.HideBottomSheet -> {
                selectedItem = null
                _showBottomSheet.value = false
            }

            MainEvent.EditNoteModal -> {
                selectedItem?.let {
                    onEditNote(it)
                }
                selectedItem = null
                _showBottomSheet.value = false
            }
        }
    }

}