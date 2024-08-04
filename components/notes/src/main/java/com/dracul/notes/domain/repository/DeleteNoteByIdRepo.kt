package com.dracul.notes.domain.repository

interface DeleteNoteByIdRepo {
    operator fun invoke(id:Long)
}