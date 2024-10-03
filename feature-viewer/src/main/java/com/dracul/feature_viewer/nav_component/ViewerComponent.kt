package com.dracul.feature_viewer.nav_component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.dracul.feature_viewer.event.ViewerAction
import com.dracul.feature_viewer.event.ViewerEvent
import com.dracul.images.domain.usecase.GetAllImagesByParentIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ViewerComponent(
    val parentId: Long,
    val index: Int,
    componentContext: ComponentContext,
    private val onGoBack: () -> Unit,
) : ComponentContext by componentContext, KoinComponent {
    private val getAllImagesByParentIdUseCase by inject<GetAllImagesByParentIdUseCase>()
    val images = getAllImagesByParentIdUseCase(parentId)
    private val backCallback = BackCallback(priority = Int.MAX_VALUE) {
        coroutineScope.launch {
        }
        onGoBack()
    }
    private val _events = MutableSharedFlow<ViewerEvent>(0)
    val events = _events.asSharedFlow()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    fun onAction(action: ViewerAction) {
        when (action) {
            ViewerAction.Exit -> {
                onGoBack()
            }
        }
    }
}