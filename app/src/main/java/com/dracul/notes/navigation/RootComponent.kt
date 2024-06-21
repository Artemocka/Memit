package com.dracul.notes.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, BackHandlerOwner {

    private val navigation = StackNavigation<Configuration>()

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.MainScreen,
        handleBackButton = true,
        childFactory = ::сhild
    )


    private fun сhild(
        config: Configuration, context: ComponentContext
    ): Child {
        return when (config) {
            is Configuration.MainScreen -> Child.MainScreen(
                MainComponent(
                    componentContext = context,
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

    fun onBackClick() {
        navigation.pop()
    }


    sealed class Child {
        data class MainScreen(val component: MainComponent) : Child()
        data class EditNote(val component: EditNoteComponent) : Child()
    }

    @Serializable
    sealed class Configuration {

        @Serializable
        data class EditNote(val id: Long?) : Configuration()

        @Serializable
        data object MainScreen : Configuration()
    }
}