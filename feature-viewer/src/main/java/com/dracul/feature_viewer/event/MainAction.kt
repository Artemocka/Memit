package com.dracul.feature_viewer.event


sealed interface ViewerAction {
    data object Exit : ViewerAction
}