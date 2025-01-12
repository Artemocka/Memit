package com.dracul.feature_viewer.mvi


sealed interface ViewerAction {
    data object Exit : ViewerAction
    data object Click : ViewerAction
    data class SetCurrentImage(val index: Int) : ViewerAction
    data class PagerTargetImage(val index: Int) : ViewerAction
    data class PagerScrollInProgress(val inProgrees: Boolean) : ViewerAction
    data class SliderScrollInProgress(val inProgrees: Boolean) : ViewerAction
    data class SlideImage(val index: Int, val pagerCurrentImage: Int, val targetPage: Int) : ViewerAction
}
