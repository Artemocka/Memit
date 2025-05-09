package com.dracul.feature_viewer.nav_component

import com.arkivanov.decompose.ComponentContext
import com.dracul.feature_viewer.mvi.ViewerIntent
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
    )

    fun onAction(action: ViewerIntent) {
        when (action) {
            ViewerIntent.Exit -> onGoBack()
            ViewerIntent.Click -> state.showUi = !state.showUi
            is ViewerIntent.SetCurrentImage -> {
                state.currentImage = action.index
            }

            is ViewerIntent.SlideImage -> {
                state.currentImage = action.index
            }

            is ViewerIntent.PagerTargetImage -> {
                if (action.index != state.currentImage)
                    state.currentSliderImage = action.index
                state.currentImage = action.index
            }

            is ViewerIntent.PagerScrollInProgress -> state.isPagerScrollInProgress =
                action.inProgress

            is ViewerIntent.SliderScrollInProgress -> state.isSliderScrollInProgress =
                action.inProgress
        }
    }
}