package com.dracul.feature_edit.event

sealed interface EditTaskEvent {
    data object ShowMediaRequest : EditTaskEvent
}

