package com.dracul.feature_viewer.event



sealed interface ViewerEvent {
    data object OnExit: ViewerEvent
}