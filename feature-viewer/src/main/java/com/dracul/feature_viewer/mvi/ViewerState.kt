package com.dracul.feature_viewer.mvi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ViewerState(
    showUi: Boolean,
    currentImage: Int,
//    event: ViewerEvent,
    isPagerScrollInProgress: Boolean,
    isSliderScrollInProgress: Boolean,
) {
    var showUi by mutableStateOf(showUi)
    var currentImage by mutableIntStateOf(currentImage)
//    var event by mutableStateOf(event)
    var isPagerScrollInProgress by mutableStateOf(false)
    var isSliderScrollInProgress by mutableStateOf(false)
}
