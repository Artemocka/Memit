package com.dracul.feature_viewer.nav_component

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.dracul.feature_viewer.event.ViewerAction
import com.dracul.images.domain.models.Image
import com.dracul.images.domain.usecase.GetAllImagesByParentIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        onGoBack()
    }

    fun onAction(action: ViewerAction) {
        when (action) {
            ViewerAction.Exit -> {
                onGoBack()
            }
        }
    }
}