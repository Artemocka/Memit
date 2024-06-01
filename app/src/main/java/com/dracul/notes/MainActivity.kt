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
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.retainedComponent
import com.dracul.notes.navigation.RootComponent
import com.dracul.notes.ui.screens.CreateNoteScreen
import com.dracul.notes.ui.screens.EditNoteScreen
import com.dracul.notes.ui.screens.MainScreen
import com.dracul.notes.ui.theme.NotesTheme


class MainActivity : ComponentActivity() {
    private val activityViewModel by viewModels<ActivityViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel
        val root = retainedComponent {
            RootComponent(it)
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            App(root = root)
        }
    }
}

@Composable
fun App(root: RootComponent) {
    NotesTheme {
        val childStack by root.childStack.subscribeAsState()
        Children(
            stack = childStack,
            animation = stackAnimation(slide(tween(250, easing = EaseInOut) ))
        ) { child ->
            when (val instance = child.instance) {
                is RootComponent.Child.CreateNote -> CreateNoteScreen(instance.component)
                is RootComponent.Child.MainScreen -> MainScreen(instance.component)
                is RootComponent.Child.EditNote -> EditNoteScreen(component = instance.component)
            }
        }
    }
}