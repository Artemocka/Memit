package com.dracul.feature_viewer.mvi


sealed interface ViewerIntent {
    data object Exit : ViewerIntent
    data object Click : ViewerIntent
    data class SetCurrentImage(val index: Int) : ViewerIntent
    data class PagerTargetImage(val index: Int) : ViewerIntent
    data class PagerScrollInProgress(val inProgress: Boolean) : ViewerIntent
    data class SliderScrollInProgress(val inProgress: Boolean) : ViewerIntent
    data class SlideImage(val index: Int, val pagerCurrentImage: Int, val targetPage: Int) :
        ViewerIntent
}
