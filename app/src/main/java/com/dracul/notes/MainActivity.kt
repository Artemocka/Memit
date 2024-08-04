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
import com.dracul.common.aliases.CommonStrings
import com.dracul.feature_edit.ui.EditNoteScreen
import com.dracul.notes.domain.models.Note
import com.dracul.notes.navigation.RootComponent
import com.dracul.notes.components.Prefs
import com.dracul.notes.ui.theme.NotesTheme
import com.dracul.feature_main.MainScreen
import com.dracul.notes.domain.usecase.InsertNoteUseCase
import com.dracul.notes.viewmodels.ActivityViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : ComponentActivity(), KoinComponent {
    private val activityViewModel by viewModels<ActivityViewModel>()
    private val insertNoteUseCase by inject<InsertNoteUseCase>()

    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewModel
        val prefs = Prefs(context = applicationContext)
        if (prefs.isFirstLaunch ) {
            insertNoteUseCase(
                Note(
                    0.toLong(),
                    title = getString( CommonStrings.tip),
                    content = getString( CommonStrings.welcome),
                    color = 0,
                    pinned = false,
                )
            )
            insertNoteUseCase(
                Note(
                    0.toLong(),
                    title = getString( CommonStrings.tip),
                    content = getString( CommonStrings.welcome2),
                    color = 0,
                    pinned = false,
                )
            )
            insertNoteUseCase(
                Note(
                    0.toLong(),
                    title = getString(CommonStrings.tip),
                    content = getString( CommonStrings.welcome3),
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


