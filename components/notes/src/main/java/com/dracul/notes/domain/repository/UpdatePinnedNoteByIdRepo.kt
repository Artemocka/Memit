package com.dracul.notes.domain.repository

interface UpdatePinnedNoteByIdRepo {
    operator fun invoke(id: Long, pinned: Boolean)
}