package com.dracul.notes.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.dracul.feature_edit.nav_component.EditNoteComponent
import com.dracul.feature_main.nav_component.MainComponent
import com.dracul.feature_viewer.nav_component.ViewerComponent
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
                MainComponent(componentContext = context,
                    onEditNote = { navigation.pushNew(Configuration.EditNote(it)) })
            )

            is Configuration.EditNote -> Child.EditNote(
                EditNoteComponent(
                    id = config.id,
                    componentContext = context,
                    onGoBack = { navigation.pop() },
                    onViewer = { parentId, index->
                        navigation.pushNew(Configuration.ViewerScreen(parentId, index))
                    }
                )
            )

            is Configuration.ViewerScreen -> Child.ViewerScreen(
                ViewerComponent(
                    componentContext = context,
                    parentId = config.parentId,
                    index = config.index,
                    onGoBack = {navigation.pop()}
                )
            )
        }
    }

    fun onDeepLink(initialItemId: Long) {
        navigation.pushNew(Configuration.EditNote(id = initialItemId))
    }

    sealed class Child {
        data class MainScreen(val component: MainComponent) : Child()
        data class EditNote(val component: EditNoteComponent) : Child()
        data class ViewerScreen(val component: ViewerComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data class EditNote(val id: Long?) : Configuration()

        @Serializable
        data object MainScreen : Configuration()

        @Serializable
        data class ViewerScreen(val parentId: Long, val index: Int) : Configuration()
    }
}