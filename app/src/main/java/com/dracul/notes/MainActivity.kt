package com.dracul.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dracul.notes.db.Note
import com.dracul.notes.ui.screens.CreateNoteScreen
import com.dracul.notes.ui.screens.EditNoteScreen
import com.dracul.notes.ui.screens.MainScreen
import com.dracul.notes.ui.theme.NotesTheme
import com.example.myapplication.DatabaseProviderWrap
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseProviderWrap.createDao(this.application)
        DatabaseProviderWrap.noteDao.insert(Note(id = 0, color = 0, title = "Список покупок", pinned = false, content = "Помидоры\nОгурцы и тд\n\na"))
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NotesTheme {

                val navController = rememberNavController()
                NavHost(navController, startDestination = Home) {
                    composable<Home> {
                        MainScreen( navController = navController)
                    }
                    composable<CreateNote> {
                        CreateNoteScreen(navController = navController)
                    }
                    composable<EditNote> {backStackEntry->
                        val id = backStackEntry.toRoute<EditNote>().id
                        EditNoteScreen(id = id,navController = navController)
                    }
                }
            }
        }
    }
}

@Serializable
object Home

@Serializable
object CreateNote

@Serializable
data class EditNote(val id: Long)