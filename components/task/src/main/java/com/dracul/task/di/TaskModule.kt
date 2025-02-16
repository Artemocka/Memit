package com.dracul.task.di

import com.dracul.task.data.repository.task.DeleteTaskByIdImpl
import com.dracul.task.data.repository.task.DeleteTaskImpl
import com.dracul.task.data.repository.task.GetAllTasksImpl
import com.dracul.task.data.repository.task.GetTaskByIdImpl
import com.dracul.task.data.repository.task.InsertTaskImpl
import com.dracul.task.data.repository.task.UpdatePinnedTaskByIdImpl
import com.dracul.task.data.repository.task.UpdateTaskImpl
import com.dracul.task.data.repository.task.UpdateWorkerByIdImpl
import com.dracul.task.domain.repository.task.DeleteTaskByIdRepo
import com.dracul.task.domain.repository.task.DeleteTaskRepo
import com.dracul.task.domain.repository.task.GetAllTasksRepo
import com.dracul.task.domain.repository.task.GetTaskByIdRepo
import com.dracul.task.domain.repository.task.InsertTaskRepo
import com.dracul.task.domain.repository.task.UpdatePinnedTaskByIdRepo
import com.dracul.task.domain.repository.task.UpdateTaskRepo
import com.dracul.task.domain.repository.task.UpdateWorkerByIdRepo
import com.dracul.task.domain.usecase.task.DeleteTaskByIdUseCase
import com.dracul.task.domain.usecase.task.DeleteTaskByIdUseCaseImpl
import com.dracul.task.domain.usecase.task.DeleteTaskUseCase
import com.dracul.task.domain.usecase.task.DeleteTaskUseCaseImpl
import com.dracul.task.domain.usecase.task.GetAllTasksUseCase
import com.dracul.task.domain.usecase.task.GetAllTasksUseCaseImpl
import com.dracul.task.domain.usecase.task.GetTaskByIdUseCase
import com.dracul.task.domain.usecase.task.GetTaskByIdUseCaseImpl
import com.dracul.task.domain.usecase.task.InsertTaskUseCase
import com.dracul.task.domain.usecase.task.InsertTaskUseCaseImpl
import com.dracul.task.domain.usecase.task.UpdatePinnedTaskByIdUseCase
import com.dracul.task.domain.usecase.task.UpdatePinnedTaskByIdUseCaseImpl
import com.dracul.task.domain.usecase.task.UpdateTaskUseCase
import com.dracul.task.domain.usecase.task.UpdateTaskUseCaseImpl
import com.dracul.task.domain.usecase.task.UpdateWorkerByIdUseCase
import com.dracul.task.domain.usecase.task.UpdateWorkerByIdUseCaseImpl
import org.koin.dsl.module


val taskModule = module {
    // repository
    single<DeleteTaskByIdRepo> {
        DeleteTaskByIdImpl()
    }
    single<DeleteTaskRepo> {
        DeleteTaskImpl()
    }
    single<GetAllTasksRepo> {
        GetAllTasksImpl()
    }
    single<GetTaskByIdRepo> {
        GetTaskByIdImpl()
    }
    single<InsertTaskRepo> {
        InsertTaskImpl()
    }
    single<UpdatePinnedTaskByIdImpl> {
        UpdatePinnedTaskByIdImpl()
    }
    single<UpdateTaskRepo> {
        UpdateTaskImpl()
    }
    single<UpdateWorkerByIdRepo> {
        UpdateWorkerByIdImpl()
    }
    single<UpdatePinnedTaskByIdRepo> {
        UpdatePinnedTaskByIdImpl()
    }

    // usecase
    single<DeleteTaskByIdUseCase> {
        DeleteTaskByIdUseCaseImpl(get())
    }
    single<DeleteTaskUseCase> {
        DeleteTaskUseCaseImpl(get())
    }
    single<GetAllTasksUseCase> {
        GetAllTasksUseCaseImpl(get())
    }
    single<GetTaskByIdUseCase> {
        GetTaskByIdUseCaseImpl(get())
    }
    single<InsertTaskUseCase> {
        InsertTaskUseCaseImpl(get())
    }
    single<UpdatePinnedTaskByIdUseCase> {
        UpdatePinnedTaskByIdUseCaseImpl(get())
    }
    single<UpdateTaskUseCase> {
        UpdateTaskUseCaseImpl(get())
    }
    single<UpdateWorkerByIdUseCase> {
        UpdateWorkerByIdUseCaseImpl(get())
    }
}