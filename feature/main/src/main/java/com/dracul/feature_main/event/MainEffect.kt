package com.dracul.feature_main.event

import com.dracul.notes.domain.models.Note

sealed interface MainEffect {
    data class ShareNote(val note: Note) : MainEffect
}