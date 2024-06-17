package com.dracul.notes.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.MainScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        config: Configuration, context: ComponentContext
    ): Child {
        return when (config) {
            Configuration.CreateNote -> Child.CreateNote(
                CreateNoteComponent(
                    componentContext = context,
                    onGoBack = {
                        navigation.pop()
                    }
                )
            )

            is Configuration.MainScreen -> Child.MainScreen(
                MainComponent(
                    componentContext = context,
                    onCreateNote = { navigation.pushNew(Configuration.CreateNote) },
                    onEditNote = { navigation.pushNew(Configuration.EditNote(it)) }
                )
            )

            is Configuration.EditNote -> Child.EditNote(
                EditNoteComponent(
                    id = config.id,
                    componentContext = context,
                    onGoBack = { navigation.pop() },
                )
            )
        }
    }


    sealed class Child {
        data class CreateNote(val component: CreateNoteComponent) : Child()
        data class MainScreen(val component: MainComponent) : Child()
        data class EditNote(val component: EditNoteComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object CreateNote : Configuration()

        @Serializable
        data class EditNote(val id: Long) : Configuration()

        @Serializable
        data object MainScreen : Configuration()
    }
}