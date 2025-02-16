package com.dracul.task.di

import com.dracul.task.data.repository.sub_task.DeleteSubTaskByParentIdRepoImpl
import com.dracul.task.data.repository.sub_task.DeleteSubTaskRepoImpl
import com.dracul.task.data.repository.sub_task.GetAllSubTaskByParentIdRepoImpl
import com.dracul.task.data.repository.sub_task.GetSubTaskByIdRepoImpl
import com.dracul.task.data.repository.sub_task.InsertSubTaskRepoImpl
import com.dracul.task.data.repository.sub_task.UpdateSubTaskRepoImpl
import com.dracul.task.domain.repository.sub_task.DeleteSubTaskByParentIdRepo
import com.dracul.task.domain.repository.sub_task.DeleteSubTaskRepo
import com.dracul.task.domain.repository.sub_task.GetAllSubTasksByParentIdRepo
import com.dracul.task.domain.repository.sub_task.GetSubTaskByIdRepo
import com.dracul.task.domain.repository.sub_task.InsertSubTaskRepo
import com.dracul.task.domain.repository.sub_task.UpdateSubTaskRepo
import com.dracul.task.domain.usecase.sub_task.DeleteSubTaskByIdImpl
import com.dracul.task.domain.usecase.sub_task.DeleteSubTaskByIdUseCase
import com.dracul.task.domain.usecase.sub_task.DeleteSubTaskImpl
import com.dracul.task.domain.usecase.sub_task.DeleteSubTaskUseCase
import com.dracul.task.domain.usecase.sub_task.GetAllSubTaskByParentIdImpl
import com.dracul.task.domain.usecase.sub_task.GetAllSubTaskByParentIdUseCase
import com.dracul.task.domain.usecase.sub_task.GetSubTaskByIdImpl
import com.dracul.task.domain.usecase.sub_task.GetSubTaskByIdUseCase
import com.dracul.task.domain.usecase.sub_task.InsertSubTaskImpl
import com.dracul.task.domain.usecase.sub_task.InsertSubTaskUseCase
import com.dracul.task.domain.usecase.sub_task.UpdateSubTaskImpl
import com.dracul.task.domain.usecase.sub_task.UpdateSubTaskUseCase
import com.dracul.task.domain.usecase.task.UpdateWorkerByIdUseCase
import com.dracul.task.domain.usecase.task.UpdateWorkerByIdUseCaseImpl
import org.koin.dsl.module


val subTaskModule = module {
    // repository
    single<DeleteSubTaskRepo> {
        DeleteSubTaskRepoImpl()
    }
    single<DeleteSubTaskByParentIdRepo> {
        DeleteSubTaskByParentIdRepoImpl()
    }
    single<DeleteSubTaskRepo> {
        DeleteSubTaskRepoImpl()
    }
    single<GetSubTaskByIdRepo> {
        GetSubTaskByIdRepoImpl()
    }
    single<InsertSubTaskRepo> {
        InsertSubTaskRepoImpl()
    }
    single<UpdateSubTaskRepo> {
        UpdateSubTaskRepoImpl()
    }
    single<GetAllSubTasksByParentIdRepo> {
        GetAllSubTaskByParentIdRepoImpl()
    }

    // usecase
    single<DeleteSubTaskByIdUseCase> {
        DeleteSubTaskByIdImpl(get())
    }
    single<DeleteSubTaskUseCase> {
        DeleteSubTaskImpl(get())
    }
    single<GetSubTaskByIdUseCase> {
        GetSubTaskByIdImpl(get())
    }
    single<InsertSubTaskUseCase> {
        InsertSubTaskImpl(get())
    }
    single<UpdateSubTaskUseCase> {
        UpdateSubTaskImpl(get())
    }
    single<UpdateWorkerByIdUseCase> {
        UpdateWorkerByIdUseCaseImpl(get())
    }
    single<GetAllSubTaskByParentIdUseCase> {
        GetAllSubTaskByParentIdImpl(get())
    }
}