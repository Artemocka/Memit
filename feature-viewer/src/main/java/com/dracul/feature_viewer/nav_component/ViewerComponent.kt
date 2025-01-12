package com.dracul.feature_viewer.nav_component

import com.arkivanov.decompose.ComponentContext
import com.dracul.feature_viewer.mvi.ViewerAction
import com.dracul.feature_viewer.mvi.ViewerEvent
import com.dracul.feature_viewer.mvi.ViewerState
import com.dracul.images.domain.usecase.GetAllImagesByParentIdUseCase
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
    var state = ViewerState(
        showUi = true,
        currentImage = index,
        isSliderScrollInProgress = false,
        isPagerScrollInProgress = false,
//        event = ViewerEvent.SlideAll(index)
    )

    fun onAction(action: ViewerAction) {
        when (action) {
            ViewerAction.Exit -> onGoBack()
            ViewerAction.Click -> state.showUi = !state.showUi
            is ViewerAction.SetCurrentImage -> state.currentImage = action.index
            is ViewerAction.SlideImage -> state.currentImage = action.index
            is ViewerAction.PagerTargetImage -> state.currentImage = action.index
            is ViewerAction.PagerScrollInProgress -> state.isPagerScrollInProgress = action.inProgrees
            is ViewerAction.SliderScrollInProgress -> state.isSliderScrollInProgress = action.inProgrees
        }
    }
}