package com.dracul.notes.di

import com.dracul.notes.data.repository.DeleteNoteByIdImpl
import com.dracul.notes.data.repository.DeleteNoteImpl
import com.dracul.notes.data.repository.GetAllNotesImpl
import com.dracul.notes.data.repository.GetNoteByIdImpl
import com.dracul.notes.data.repository.InsertNoteImpl
import com.dracul.notes.data.repository.UpdateNoteImpl
import com.dracul.notes.data.repository.UpdatePinnedNoteByIdImpl
import com.dracul.notes.domain.repository.DeleteNoteByIdRepo
import com.dracul.notes.domain.repository.DeleteNoteRepo
import com.dracul.notes.domain.repository.GetAllNotesRepo
import com.dracul.notes.domain.repository.GetNoteByIdRepo
import com.dracul.notes.domain.repository.InsertNoteRepo
import com.dracul.notes.domain.repository.UpdateNoteRepo
import com.dracul.notes.domain.repository.UpdatePinnedNoteByIdRepo
import com.dracul.notes.domain.usecase.DeleteNoteByIdUseCase
import com.dracul.notes.domain.usecase.DeleteNoteByIdUseCaseImpl
import com.dracul.notes.domain.usecase.DeleteNoteUseCase
import com.dracul.notes.domain.usecase.DeleteNoteUseCaseImpl
import com.dracul.notes.domain.usecase.GetAllNotesUseCase
import com.dracul.notes.domain.usecase.GetAllNotesUseCaseImpl
import com.dracul.notes.domain.usecase.GetNoteByIdUseCase
import com.dracul.notes.domain.usecase.GetNoteByIdUseCaseImpl
import com.dracul.notes.domain.usecase.InsertNoteUseCase
import com.dracul.notes.domain.usecase.InsertNoteUseCaseImpl
import com.dracul.notes.domain.usecase.UpdateNoteUseCase
import com.dracul.notes.domain.usecase.UpdateNoteUseCaseImpl
import com.dracul.notes.domain.usecase.UpdatePinnedNoteByIdUseCase
import com.dracul.notes.domain.usecase.UpdatePinnedNoteByIdUseCaseImpl
import org.koin.dsl.module

val notesModule = module {
    //repository
    single<GetNoteByIdRepo> {
        GetNoteByIdImpl()
    }
    single<GetAllNotesRepo> {
        GetAllNotesImpl()
    }
    single<InsertNoteRepo> {
        InsertNoteImpl()
    }
    single<UpdateNoteRepo> {
        UpdateNoteImpl()
    }
    single<UpdatePinnedNoteByIdRepo> {
        UpdatePinnedNoteByIdImpl()
    }
    single<DeleteNoteRepo> {
        DeleteNoteImpl()
    }
    single<DeleteNoteByIdRepo> {
        DeleteNoteByIdImpl()
    }
    //usecase
    single<GetNoteByIdUseCase> {
        GetNoteByIdUseCaseImpl(get())
    }
    single<GetAllNotesUseCase> {
        GetAllNotesUseCaseImpl(get())
    }
    single<InsertNoteUseCase> {
        InsertNoteUseCaseImpl(get())
    }
    single<UpdateNoteUseCase> {
        UpdateNoteUseCaseImpl(get())
    }
    single<UpdatePinnedNoteByIdUseCase> {
        UpdatePinnedNoteByIdUseCaseImpl(get())
    }
    single<DeleteNoteUseCase> {
        DeleteNoteUseCaseImpl(get())
    }
    single<DeleteNoteByIdUseCase> {
        DeleteNoteByIdUseCaseImpl(get())
    }
}