package com.dracul.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.retainedComponent
import com.dracul.notes.data.Note
import com.dracul.notes.navigation.RootComponent
import com.dracul.notes.ui.components.Prefs
import com.dracul.notes.ui.screens.EditNoteScreen
import com.dracul.notes.ui.screens.MainScreen
import com.dracul.notes.ui.theme.NotesTheme
import com.example.myapplication.DatabaseProviderWrap


class MainActivity : ComponentActivity() {
    private val activityViewModel by viewModels<ActivityViewModel>()

    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel
        val prefs = Prefs(context = applicationContext)
        if (prefs.isFirstLaunch ) {
            DatabaseProviderWrap.noteDao.insert(
                Note(
                    0.toLong(),
                    title = getString(R.string.tip),
                    content = getString(R.string.welcome),
                    color = 0,
                    pinned = true,
                )
            )
            DatabaseProviderWrap.noteDao.insert(
                Note(
                    0.toLong(),
                    title = getString(R.string.tip),
                    content = getString(R.string.welcome_2),
                    color = 0,
                    pinned = true,
                )
            )
        }
        val root = retainedComponent {
            RootComponent(it)
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT
            )
        )

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            App(component = root)
        }
    }
}

@Composable
fun App(component: RootComponent) {
    NotesTheme {
        Children(
            stack = component.childStack, animation = stackAnimation(
                fade(tween(300, easing = EaseInOut)) + slide(tween(300, easing = EaseInOut))
            )
        ) { child ->
            when (val instance = child.instance) {
                is RootComponent.Child.MainScreen -> MainScreen(instance.component)
                is RootComponent.Child.EditNote -> EditNoteScreen(component = instance.component)
            }
        }
    }
}


