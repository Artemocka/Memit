package com.dracul.feature_viewer.mvi


sealed interface ViewerEvent {
    data class SlideRow(val index: Int) : ViewerEvent
    data class SlidePager(val index: Int) : ViewerEvent
    data class SlideAll(val index: Int) : ViewerEvent
}